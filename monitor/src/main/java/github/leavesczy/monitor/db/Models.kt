package github.leavesczy.monitor.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import github.leavesczy.monitor.utils.FormatUtils

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal data class HttpHeader(val name: String, val value: String)

internal enum class HttpStatus {
    Requested,
    Complete,
    Failed
}

@Entity(tableName = MonitorDatabase.MonitorTableName)
internal data class HttpInformation(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val url: String,
    val host: String,
    val path: String,
    val scheme: String,
    val protocol: String,
    val method: String,
    val requestHeaders: List<HttpHeader>,
    val requestBody: String,
    val requestContentType: String,
    val requestContentLength: Long,
    val requestDate: Long,
    val responseHeaders: List<HttpHeader>,
    val responseBody: String,
    val responseContentType: String,
    val responseContentLength: Long,
    val responseDate: Long,
    val responseTlsVersion: String,
    val responseCipherSuite: String,
    val responseCode: Int = DEFAULT_RESPONSE_CODE,
    val responseMessage: String,
    val error: String?
) {

    companion object {

        private const val DEFAULT_RESPONSE_CODE = -100

    }

    val isSsl: Boolean
        get() = "https".equals(scheme, ignoreCase = true)

    val httpStatus: HttpStatus
        get() = when {
            error != null -> {
                HttpStatus.Failed
            }

            responseCode == DEFAULT_RESPONSE_CODE -> {
                HttpStatus.Requested
            }

            else -> {
                HttpStatus.Complete
            }
        }

    val notificationText: String
        get() {
            return when (httpStatus) {
                HttpStatus.Failed -> {
                    "!!!$path"
                }

                HttpStatus.Requested -> {
                    "...$path"
                }

                else -> {
                    "$responseCode $path"
                }
            }
        }

    val responseSummaryText: String
        get() {
            return when (httpStatus) {
                HttpStatus.Failed -> {
                    error ?: ""
                }

                HttpStatus.Requested -> {
                    ""
                }

                else -> {
                    "$responseCode $responseMessage"
                }
            }
        }

    val requestBodyFormat: String
        get() = FormatUtils.formatBody(requestBody, requestContentType)

    val responseBodyFormat: String
        get() = FormatUtils.formatBody(responseBody, responseContentType)

    val responseCodeFormat: String
        get() {
            return when (httpStatus) {
                HttpStatus.Requested -> {
                    "..."
                }

                HttpStatus.Complete -> {
                    responseCode.toString()
                }

                HttpStatus.Failed -> {
                    "!!!"
                }
            }
        }

    val requestDateFormatLong: String
        get() = FormatUtils.getDateFormatLong(requestDate)

    val requestDateFormatShort: String
        get() = FormatUtils.getDateFormatShort(requestDate)

    val responseDateFormatLong: String
        get() = FormatUtils.getDateFormatLong(responseDate)

    val durationFormat: String
        get() {
            val request = requestDate
            val response = responseDate
            if (request <= 0 || response <= 0) {
                return ""
            }
            return when (httpStatus) {
                HttpStatus.Requested -> {
                    ""
                }

                HttpStatus.Complete -> {
                    "${response - request} ms"
                }

                HttpStatus.Failed -> {
                    ""
                }
            }
        }

    val totalSizeFormat: String
        get() {
            return when (httpStatus) {
                HttpStatus.Requested -> {
                    ""
                }

                HttpStatus.Complete -> {
                    FormatUtils.formatBytes(requestContentLength + responseContentLength)
                }

                HttpStatus.Failed -> {
                    ""
                }
            }
        }

    fun getRequestHeadersString(withMarkup: Boolean): String {
        return FormatUtils.formatHeaders(requestHeaders, withMarkup)
    }

    fun getResponseHeadersString(withMarkup: Boolean): String {
        return FormatUtils.formatHeaders(responseHeaders, withMarkup)
    }

}