package org.ignus.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.ignus.App
import org.ignus.R
import org.ignus.db.models.Workshop
import org.ignus.utils.formatDate
import org.ignus.utils.openGoogleMaps


class ZWorkshopListAdapter(private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<ZWorkshopListAdapter.MyViewHolder>() {

    private var mExpandedPosition = -1
    private var list: List<Workshop> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workshop_list_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(list[position])

        val isExpanded = position == mExpandedPosition
        holder.layout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.expand.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            TransitionManager.beginDelayedTransition(recyclerView)
            notifyDataSetChanged()
        }

        if (isExpanded) holder.expand.setImageResource(R.drawable.ic_expand_less)
        else holder.expand.setImageResource(R.drawable.ic_expand_more)
    }

    fun setList(list: List<Workshop>) {
        this.list = list
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bgImg: ImageView = view.findViewById(R.id.bg_img)
        private val title: TextView = view.findViewById(R.id.name)
        private val location: TextView = view.findViewById(R.id.location)
        private val time: TextView = view.findViewById(R.id.time)
        private val about: TextView = view.findViewById(R.id.about)
        private val details: TextView = view.findViewById(R.id.details)

        val expand: ImageView = view.findViewById(R.id.expand)
        val layout: ConstraintLayout = view.findViewById(R.id.test)

        fun bindData(workshop: Workshop) {

            Glide.with(bgImg)
                .load(workshop.cover)
                .apply(
                    RequestOptions()
                        .placeholder(ColorDrawable(Color.BLACK))
                        .error(R.drawable.placeholder)
                )
                .into(bgImg)

            title.text = workshop.name
            location.text = workshop.location?.name
            time.text = workshop.start_time?.formatDate

            val x = App.instance.getString(R.string.workshop_about, workshop.about)
            about.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(x, Html.FROM_HTML_MODE_LEGACY)?.trim()
            else Html.fromHtml(x)?.trim()

            if (workshop.details != null) {
                val y = App.instance.getString(R.string.workshop_details, workshop.details)
                about.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    Html.fromHtml(y, Html.FROM_HTML_MODE_LEGACY)?.trim()
                else Html.fromHtml(y)?.trim()
                details.visibility = View.VISIBLE
            }

            location.setOnClickListener {
                openGoogleMaps(workshop.location)
            }
        }
    }
}