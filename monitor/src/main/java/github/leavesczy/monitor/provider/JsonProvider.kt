package github.leavesczy.monitor.provider

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal object JsonProvider {

    private var gson = GsonBuilder()
        .setPrettyPrinting()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    fun setPrettyPrinting(json: String): String {
        return try {
            gson.toJson(JsonParser.parseString(json))
        } catch (e: Exception) {
            json
        }
    }

    fun toJson(ob: Any): String {
        return gson.toJson(ob)
    }

    fun <T> fromJsonArray(json: String, clazz: Class<T>): List<T> {
        val type = ParameterizedTypeImpl(clazz)
        var ob: List<T>? = gson.fromJson<List<T>>(json, type)
        if (ob == null) {
            ob = ArrayList()
        }
        return ob
    }

    private class ParameterizedTypeImpl<T> constructor(val clazz: Class<T>) : ParameterizedType {

        override fun getActualTypeArguments(): Array<Type> {
            return arrayOf(clazz)
        }

        override fun getRawType(): Type {
            return List::class.java
        }

        override fun getOwnerType(): Type? {
            return null
        }
    }

}