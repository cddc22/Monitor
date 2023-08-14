package github.leavesczy.monitor.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import github.leavesczy.monitor.R
import github.leavesczy.monitor.logic.MonitorDetailViewModel

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorResponseFragment : Fragment() {

    companion object {

        fun newInstance(): MonitorResponseFragment {
            return MonitorResponseFragment()
        }
    }

    private lateinit var tvHeaders: TextView

    private lateinit var tvBody: TextView

    private val monitorDetailViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity())[MonitorDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_monitor_response, container, false)
        tvHeaders = view.findViewById(R.id.tvHeaders)
        tvBody = view.findViewById(R.id.tvBody)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monitorDetailViewModel.recordLiveData.observe(viewLifecycleOwner) { information ->
            val headersString = information.getResponseHeadersString(true)
            if (headersString.isBlank()) {
                tvHeaders.visibility = View.GONE
            } else {
                tvHeaders.visibility = View.VISIBLE
                tvHeaders.text = Html.fromHtml(headersString)
            }
            tvBody.text = information.responseBodyFormat
        }
    }

}