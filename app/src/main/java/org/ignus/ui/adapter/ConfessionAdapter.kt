package org.ignus.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.ignus.R
import org.ignus.db.models.Message
import org.ignus.db.viewmodels.ConfessionViewModel
import org.ignus.utils.formatShortTime
import org.ignus.utils.px

class ConfessionAdapter(private val model: ConfessionViewModel) :
    RecyclerView.Adapter<ConfessionAdapter.MyViewHolder>() {

    private var list: List<Message> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.confession_message, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setData(list: List<Message>) {
        this.list = list
        notifyDataSetChanged()
    }

    private var username: String = ""

    fun setUsername(username: String) {
        this.username = username
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val text: TextView = view.findViewById(R.id.text)
        private val time: TextView = view.findViewById(R.id.time)
        private val clear: ImageView = view.findViewById(R.id.delete)

        fun bindData(message: Message) {
            text.text = message.message
            time.text = message.timestamp?.formatShortTime

            if (username == message.username && System.currentTimeMillis() - (message.timestamp ?: 0) < 7 * 60 * 1000) {
                clear.visibility = View.VISIBLE
                text.setPadding(text.paddingLeft, text.paddingTop, 36.px, text.paddingBottom)
                clear.setOnClickListener {
                    if (System.currentTimeMillis() - (message.timestamp ?: 0) < 7 * 60 * 1000)
                        model.deleteMessage(message.key ?: return@setOnClickListener)
                    else notifyDataSetChanged()
                }
            } else {
                clear.visibility = View.GONE
                val p = text.paddingLeft
                text.setPadding(p, p, p, p)
            }
        }
    }
}