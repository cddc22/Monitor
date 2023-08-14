package github.leavesczy.monitor.logic

import androidx.lifecycle.ViewModel
import github.leavesczy.monitor.db.MonitorDatabase

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorDetailViewModel(id: Long) : ViewModel() {

    val recordLiveData by lazy(mode = LazyThreadSafetyMode.NONE) {
        MonitorDatabase.instance.monitorDao.queryRecord(id)
    }

}