package github.leavesczy.monitor.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import github.leavesczy.monitor.R
import github.leavesczy.monitor.adapter.MonitorAdapter
import github.leavesczy.monitor.adapter.OnItemClickListener
import github.leavesczy.monitor.db.HttpInformation
import github.leavesczy.monitor.db.MonitorDatabase
import github.leavesczy.monitor.provider.NotificationProvider
import github.leavesczy.monitor.viewmodel.MonitorViewModel
import kotlin.concurrent.thread

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorActivity : AppCompatActivity() {

    private val monitorViewModel by lazy {
        ViewModelProvider(this)[MonitorViewModel::class.java].apply {
            allRecordLiveData.observe(this@MonitorActivity) {
                monitorAdapter.setData(it)
            }
        }
    }

    private val monitorAdapter by lazy {
        MonitorAdapter(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)
        initView()
        monitorViewModel.init()
    }

    private fun initView() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.monitor_lib_name)
        }
        monitorAdapter.clickListener = object : OnItemClickListener {
            override fun onClick(position: Int, model: HttpInformation) {
                MonitorDetailsActivity.navTo(this@MonitorActivity, model.id)
            }
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = monitorAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_monitor_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> {
                thread {
                    MonitorDatabase.instance.monitorDao.deleteAll()
                }
                NotificationProvider.clearBuffer()
                NotificationProvider.dismiss()
            }

            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

}