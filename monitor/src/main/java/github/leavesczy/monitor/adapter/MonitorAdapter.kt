package github.leavesczy.monitor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import github.leavesczy.monitor.R
import github.leavesczy.monitor.db.HttpInformation
import github.leavesczy.monitor.db.HttpStatus

/**
 * @Author: leavesCZY
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
internal class MonitorAdapter(private val context: Context) :
    RecyclerView.Adapter<MonitorViewHolder>() {

    private val colorSuccess = getColor(R.color.monitor_status_success)

    private val colorRequested = getColor(R.color.monitor_status_requested)

    private val colorError = getColor(R.color.monitor_status_error)

    private val color300 = getColor(R.color.monitor_status_300)

    private val color400 = getColor(R.color.monitor_status_400)

    private val color500 = getColor(R.color.monitor_status_500)

    private val asyncListDiffer = AsyncListDiffer(this, MonitorDiffUtilItemCallback())

    var clickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MonitorViewHolder {
        return MonitorViewHolder(viewGroup)
    }

    override fun onBindViewHolder(holder: MonitorViewHolder, position: Int) {
        val httpInformation = asyncListDiffer.currentList[position]
        holder.tvId.text = httpInformation.id.toString()
        holder.tvPath.text = String.format("%s  %s", httpInformation.method, httpInformation.path)
        holder.tvHost.text = httpInformation.host
        holder.tvRequestDate.text = httpInformation.requestDateFormatShort
        holder.ivSsl.visibility = if (httpInformation.isSsl) View.VISIBLE else View.GONE
        holder.tvCode.text = httpInformation.responseCodeFormat
        holder.tvDuration.text = httpInformation.durationFormat
        holder.tvSize.text = httpInformation.totalSizeFormat
        setStatusColor(holder, httpInformation)
        holder.view.setOnClickListener {
            clickListener?.onClick(holder.adapterPosition, httpInformation)
        }
    }

    private fun setStatusColor(holder: MonitorViewHolder, httpInformation: HttpInformation) {
        val color = when {
            httpInformation.httpStatus == HttpStatus.Failed -> {
                colorError
            }

            httpInformation.httpStatus == HttpStatus.Requested -> {
                colorRequested
            }

            httpInformation.responseCode >= 500 -> {
                color500
            }

            httpInformation.responseCode >= 400 -> {
                color400
            }

            httpInformation.responseCode >= 300 -> {
                color300
            }

            else -> {
                colorSuccess
            }
        }
        holder.tvCode.setTextColor(color)
        holder.tvPath.setTextColor(color)
        holder.tvId.setTextColor(color)
        holder.tvHost.setTextColor(color)
        holder.tvRequestDate.setTextColor(color)
        holder.tvDuration.setTextColor(color)
        holder.tvSize.setTextColor(color)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    fun setData(dataList: List<HttpInformation>) {
        asyncListDiffer.submitList(dataList)
    }

    fun clear() {
        asyncListDiffer.submitList(null)
    }

    private fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(context, id)
    }

}

internal interface OnItemClickListener {

    fun onClick(position: Int, model: HttpInformation)

}

private class MonitorDiffUtilItemCallback : DiffUtil.ItemCallback<HttpInformation>() {

    override fun areItemsTheSame(oldItem: HttpInformation, newItem: HttpInformation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: HttpInformation,
        newItem: HttpInformation
    ): Boolean {
        return oldItem == newItem
    }

}

internal class MonitorViewHolder(viewGroup: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(viewGroup.context).inflate(R.layout.item_monitor, viewGroup, false)
) {

    val view: View = itemView
    val tvId: TextView = view.findViewById(R.id.tvId)
    val tvCode: TextView = view.findViewById(R.id.tvCode)
    val tvPath: TextView = view.findViewById(R.id.tvPath)
    val tvHost: TextView = view.findViewById(R.id.tvHost)
    val ivSsl: ImageView = view.findViewById(R.id.ivSsl)
    val tvRequestDate: TextView = view.findViewById(R.id.tvRequestDate)
    val tvDuration: TextView = view.findViewById(R.id.tvDuration)
    val tvSize: TextView = view.findViewById(R.id.tvSize)

}