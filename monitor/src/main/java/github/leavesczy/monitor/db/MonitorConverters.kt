package github.leavesczy.monitor.db

import androidx.room.TypeConverter
import github.leavesczy.monitor.provider.SerializableProvider
import java.util.Date

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toHttpHeaderList(json: String): List<HttpHeader> {
        return SerializableProvider.fromJsonArray(json, HttpHeader::class.java)
    }

    @TypeConverter
    fun toJsonFromHttpHeaderList(list: List<HttpHeader>): String {
        return SerializableProvider.toJson(list)
    }

}