package github.leavesczy.monitor.viewmodel

import androidx.lifecycle.ViewModel
import github.leavesczy.monitor.db.MonitorDatabase

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorDetailViewModel(id: Long) : ViewModel() {

    val recordLiveData by lazy {
        MonitorDatabase.instance.monitorDao.queryRecordObservable(id)
    }

    fun init() {

    }

    fun queryRecordById() {

    }

}