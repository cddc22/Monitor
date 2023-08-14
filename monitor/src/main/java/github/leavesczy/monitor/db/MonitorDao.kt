package github.leavesczy.monitor.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Dao
internal interface MonitorDao {

    @Insert
    fun insert(model: HttpInformation): Long

    @Update
    fun update(model: HttpInformation)

    @Query("SELECT * FROM ${MonitorDatabase.MonitorTableName} WHERE id =:id")
    fun queryRecord(id: Long): LiveData<HttpInformation>

    @Query("SELECT * FROM ${MonitorDatabase.MonitorTableName} order by id desc limit :limit")
    fun queryRecord(limit: Int): LiveData<List<HttpInformation>>

    @Query("DELETE FROM ${MonitorDatabase.MonitorTableName}")
    fun deleteAll()

}