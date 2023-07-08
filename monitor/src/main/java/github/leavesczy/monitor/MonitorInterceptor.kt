package github.leavesczy.monitor

import android.content.Context
import android.net.Uri
import github.leavesczy.monitor.db.HttpHeader
import github.leavesczy.monitor.db.HttpInformation
import github.leavesczy.monitor.db.MonitorDatabase
import github.leavesczy.monitor.provider.ContextProvider
import github.leavesczy.monitor.provider.NotificationProvider
import github.leavesczy.monitor.utils.ResponseUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class MonitorInterceptor(context: Context) : Interceptor {

    init {
        ContextProvider.inject(context = context)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var httpInformation = buildHttpInformation(request = request)
        val id = insert(httpInformation = httpInformation)
        httpInformation = httpInformation.copy(id = id)
        val response: Response
        try {
            response = chain.proceed(request)
            try {
                httpInformation = processResponse(
                    response = response,
                    httpInformation = httpInformation
                )
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        } catch (e: Throwable) {
            httpInformation = httpInformation.copy(error = e.toString())
            throw e
        } finally {
            try {
                update(httpInformation = httpInformation)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return response
    }

    private fun buildHttpInformation(request: Request): HttpInformation {
        val requestDate = System.currentTimeMillis()
        val requestBody = request.body
        val url = request.url.toString()
        val uri = Uri.parse(url)
        val host = uri.host ?: ""
        val path = (uri.path ?: "") + if (uri.query.isNullOrBlank()) {
            ""
        } else {
            "?" + uri.query
        }
        val scheme = uri.scheme ?: ""
        val method = request.method
        val requestHeaders = request.headers.map {
            HttpHeader(it.first, it.second)
        }
        val mRequestBody =
            if (requestBody != null && ResponseUtils.bodyHasSupportedEncoding(request.headers)) {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                if (ResponseUtils.isProbablyUtf8(buffer)) {
                    val charset =
                        requestBody.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
                    val content = buffer.readString(charset)
                    content
                } else {
                    ""
                }
            } else {
                ""
            }
        val requestContentLength = requestBody?.contentLength() ?: 0
        val requestContentType = requestBody?.contentType()?.toString() ?: ""
        return HttpInformation(
            id = 0L,
            url = url,
            host = host,
            path = path,
            scheme = scheme,
            requestDate = requestDate,
            method = method,
            requestHeaders = requestHeaders,
            requestContentLength = requestContentLength,
            requestContentType = requestContentType,
            requestBody = mRequestBody,
            protocol = "",
            responseHeaders = emptyList(),
            responseBody = "",
            responseContentType = "",
            responseContentLength = 0L,
            responseDate = 0L,
            responseTlsVersion = "",
            responseCipherSuite = "",
            responseMessage = "",
            error = null
        )
    }

    private fun processResponse(
        response: Response,
        httpInformation: HttpInformation
    ): HttpInformation {
        val requestHeaders = response.request.headers.map {
            HttpHeader(it.first, it.second)
        }
        val responseHeaders = response.headers.map {
            HttpHeader(it.first, it.second)
        }
        val responseBody = response.body
        val responseContentType: String
        var responseContentLength = 0L
        var mResponseBody = "(encoded body omitted)"
        if (responseBody != null) {
            responseContentType = responseBody.contentType()?.toString() ?: ""
            responseContentLength = responseBody.contentLength()
            if (response.promisesBody()) {
                val encodingIsSupported =
                    ResponseUtils.bodyHasSupportedEncoding(response.headers)
                if (encodingIsSupported) {
                    val buffer = ResponseUtils.getNativeSource(response)
                    responseContentLength = buffer.size
                    if (ResponseUtils.isProbablyUtf8(buffer)) {
                        if (responseBody.contentLength() != 0L) {
                            val charset =
                                responseBody.contentType()?.charset(Charsets.UTF_8)
                                    ?: Charsets.UTF_8
                            mResponseBody = buffer.clone().readString(charset)
                        }
                    }
                }
            }
        } else {
            responseContentType = ""
        }
        return httpInformation.copy(
            requestDate = response.sentRequestAtMillis,
            responseDate = response.receivedResponseAtMillis,
            protocol = response.protocol.toString(),
            responseCode = response.code,
            responseMessage = response.message,
            responseTlsVersion = response.handshake?.tlsVersion?.javaName ?: "",
            responseCipherSuite = response.handshake?.cipherSuite?.javaName ?: "",
            requestHeaders = requestHeaders,
            responseHeaders = responseHeaders,
            responseContentType = responseContentType,
            responseContentLength = responseContentLength,
            responseBody = mResponseBody
        )
    }

    private fun insert(httpInformation: HttpInformation): Long {
        val id = MonitorDatabase.instance.monitorDao.insert(model = httpInformation)
        NotificationProvider.show(httpInformation = httpInformation.copy(id = id))
        return id
    }

    private fun update(httpInformation: HttpInformation) {
        NotificationProvider.show(httpInformation = httpInformation)
        MonitorDatabase.instance.monitorDao.update(model = httpInformation)
    }

}