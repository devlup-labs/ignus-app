package org.ignus.app.db.utils

import com.google.gson.annotations.SerializedName

data class LoginCredentials(
        @SerializedName("username") val username: String = "",
        @SerializedName("password") val password: String = ""
)