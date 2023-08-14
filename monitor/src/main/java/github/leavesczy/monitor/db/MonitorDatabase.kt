package github.leavesczy.monitor.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import github.leavesczy.monitor.provider.ContextProvider

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Database(
    entities = [HttpInformation::class],
    version = 14,
    exportSchema = true
)
@TypeConverters(MonitorTypeConverter::class)
internal abstract class MonitorDatabase : RoomDatabase() {

    companion object {

        private const val DB_NAME = "Monitor.db"

        internal const val MonitorTableName = "MonitorHttpTable"

        private var monitorDatabase: MonitorDatabase? = null

        val instance: MonitorDatabase
            get() {
                return monitorDatabase ?: synchronized(lock = this) {
                    val cache = monitorDatabase
                    if (cache != null) {
                        return@synchronized cache
                    }
                    val db = createDb(context = ContextProvider.context)
                    monitorDatabase = db
                    return@synchronized db
                }
            }

        private fun createDb(context: Context): MonitorDatabase {
            return Room.databaseBuilder(
                context,
                MonitorDatabase::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration()
                .build()
        }

    }

    abstract val monitorDao: MonitorDao

}