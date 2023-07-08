package github.leavesczy.monitor.provider

import android.annotation.SuppressLint
import android.content.Context

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@SuppressLint("StaticFieldLeak")
internal object ContextProvider {

    lateinit var context: Context
        private set

    fun inject(context: Context) {
        this.context = context
    }

}