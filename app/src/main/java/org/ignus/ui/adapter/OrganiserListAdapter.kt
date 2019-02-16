package org.ignus.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.ignus.App
import org.ignus.R
import org.ignus.db.models.Organiser
import java.util.*

class OrganiserListAdapter(private val organisers: List<Organiser>) :
    RecyclerView.Adapter<OrganiserListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganiserListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.organiser_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = organisers.size

    override fun onBindViewHolder(holder: OrganiserListAdapter.MyViewHolder, position: Int) {
        holder.bindDate(organisers[position])
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.name)
        private val phone: TextView = view.findViewById(R.id.phone)
        private val avatar: ImageView = view.findViewById(R.id.avatar)
        private val call: ImageView = view.findViewById(R.id.call)
        private val mail: ImageView = view.findViewById(R.id.mail)

        fun bindDate(organiser: Organiser) {

            name.text = organiser.name
            var phoneNumber = organiser.phone ?: ""
            if (phoneNumber.length <= 10) phoneNumber = "+91$phoneNumber"
            phone.text = PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country)
            Glide.with(avatar).load(organiser.avatar_url)
                .apply(RequestOptions.circleCropTransform()
                    .placeholder(ColorDrawable(Color.BLACK)))
                .into(avatar)

            call.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + organiser.phone))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                App.instance.startActivity(intent)
            }

            mail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.type = "message/rfc822"
                intent.data = Uri.parse("mailto:${organiser.email}")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(organiser.email))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Ignus 2019")
                intent.putExtra(Intent.EXTRA_TEXT, "Hi ${organiser.name}")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                App.instance.startActivity(intent)
            }

        }
    }
}