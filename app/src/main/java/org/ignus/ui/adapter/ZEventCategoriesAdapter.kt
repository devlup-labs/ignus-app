package org.ignus.ui.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.ignus.R
import org.ignus.db.models.EventCategory


class ZEventCategoriesAdapter(private val activity: Activity, private var data: List<EventCategory> = emptyList()) :
    RecyclerView.Adapter<ZEventCategoriesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_categories_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(activity, position, data)
    }

    fun setData(newData: List<EventCategory>) {
        this.data = newData
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val cover: ImageView = view.findViewById(R.id.imageView)
        private val title: TextView = view.findViewById(R.id.textView)

        fun bindData(activity: Activity, position: Int, data: List<EventCategory>) {

            Glide.with(cover)
                .load(data[position].cover)
                .apply(
                    RequestOptions()
                        .placeholder(ColorDrawable(Color.BLACK))
                        .error(R.drawable.placeholder)
                )
                .into(cover)

            title.text = data[position].name

            cover.setOnClickListener {

                val bundle = Bundle().apply {
                    putSerializable("event-category", data[position])
                }

                val mainNavView = activity.findViewById<View>(R.id.nav_host_fragment)
                val navController = Navigation.findNavController(mainNavView)
                navController.navigate(R.id.eventListFragment, bundle)

            }

            // cover.transitionName = "transition$position"

            /*cover.setOnClickListener {

                val intent = Intent(cover.context, EventListActivity::class.java)
                intent.putExtra("EventCategory", data[position])
                intent.putExtra("parent_type", data[position].parentType)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    cover.context as MainActivity,
                    cover, "transition$position"
                )

                cover.context.startActivity(intent, options.toBundle())
            }*/
        }
    }
}