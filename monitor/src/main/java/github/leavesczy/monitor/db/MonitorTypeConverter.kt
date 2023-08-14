package github.leavesczy.monitor.db

import androidx.room.TypeConverter
import github.leavesczy.monitor.provider.JsonProvider

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorTypeConverter {

    @TypeConverter
    fun fromJsonArray(json: String): List<HttpHeader> {
        return JsonProvider.fromJsonArray(json, HttpHeader::class.java)
    }

    @TypeConverter
    fun toJson(list: List<HttpHeader>): String {
        return JsonProvider.toJson(list)
    }

}