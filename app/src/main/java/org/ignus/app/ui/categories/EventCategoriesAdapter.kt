package org.ignus.app.ui.categories

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import org.ignus.app.R
import org.ignus.app.db.models.EventCategory
import org.ignus.app.ui.MainActivity
import org.ignus.app.ui.events.EventListActivity

class EventCategoriesAdapter(private val data: List<EventCategory>) : RecyclerView.Adapter<EventCategoriesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_categories_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(position, data)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val cover: ImageView = view.findViewById(R.id.imageView)
        private val title: TextView = view.findViewById(R.id.textView)

        fun bindData(position: Int, data: List<EventCategory>) {
            title.text = data[position].name

            cover.transitionName = "transition$position"

            cover.setOnClickListener {
                val intent = Intent(cover.context, EventListActivity::class.java)
                intent.putExtra("EventCategory", data[position])
                intent.putExtra("parent_type", data[position].parentType)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(cover.context as MainActivity,
                        cover, "transition$position")

                cover.context.startActivity(intent, options.toBundle())
            }
        }
    }
}