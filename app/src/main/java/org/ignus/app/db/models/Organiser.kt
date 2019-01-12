package org.ignus.app.db.models

import com.google.gson.annotations.SerializedName

data class Organiser(@SerializedName("name")
                     val name: String = "",
                     @SerializedName("phone")
                     val phone: String = "",
                     @SerializedName("email")
                     val email: String = "",
                     @SerializedName("avatar_url")
                     val avatarUrl: String = "")