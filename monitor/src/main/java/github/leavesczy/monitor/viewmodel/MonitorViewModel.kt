package github.leavesczy.monitor.viewmodel

import androidx.lifecycle.ViewModel
import github.leavesczy.monitor.db.MonitorDatabase

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorViewModel : ViewModel() {

    companion object {

        private const val LIMIT = 300

    }

    val allRecordLiveData by lazy {
        MonitorDatabase.instance.monitorDao.queryAllRecordObservable(
            LIMIT
        )
    }

    fun init() {

    }

}