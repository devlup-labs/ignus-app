package org.ignus.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.ignus.App
import org.ignus.R
import org.ignus.db.models.Developer


class DeveloperAdapter(private val list: List<Developer>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return if (type == 0) MyViewHolder0(
            LayoutInflater.from(parent.context).inflate(
                R.layout.developer_header,
                parent,
                false
            )
        )
        else MyViewHolder1(LayoutInflater.from(parent.context).inflate(R.layout.developer_card, parent, false))
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            0 -> (holder as MyViewHolder0).bindData(list[position])
            1 -> (holder as MyViewHolder1).bindData(list[position])
        }
    }

    override fun getItemViewType(position: Int) = if (list[position].designation != null) 0 else 1

    inner class MyViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val mail = view.findViewById<ImageView>(R.id.mail)
        private val github = view.findViewById<ImageView>(R.id.github)

        fun bindData(developer: Developer) {
            name.text = developer.name

            mail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.type = "message/rfc822"
                intent.data = Uri.parse("mailto:${developer.email}")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(developer.email))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Ignus 2019")
                intent.putExtra(Intent.EXTRA_TEXT, "Hi ${developer.name}")
                try {
                    App.instance.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(App.instance, "Mail ID not properly formatted!", Toast.LENGTH_SHORT).show()
                }
            }
            github.setOnClickListener {
                val url = developer.github
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                try {
                    App.instance.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(App.instance, "URL not properly formatted!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    inner class MyViewHolder0(view: View) : RecyclerView.ViewHolder(view) {
        private val designation = view.findViewById<TextView>(R.id.designation)

        fun bindData(developer: Developer) {
            designation.text = developer.designation
        }
    }
}