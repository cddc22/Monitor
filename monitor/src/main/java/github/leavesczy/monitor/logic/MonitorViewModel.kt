package github.leavesczy.monitor.logic

import androidx.lifecycle.ViewModel
import github.leavesczy.monitor.db.MonitorDatabase

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorViewModel : ViewModel() {

    companion object {

        private const val COUNT_LIMIT = 300

    }

    val allRecordLiveData by lazy(mode = LazyThreadSafetyMode.NONE) {
        MonitorDatabase.instance.monitorDao.queryRecord(COUNT_LIMIT)
    }

}