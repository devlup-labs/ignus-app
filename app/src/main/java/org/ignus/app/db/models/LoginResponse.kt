package org.ignus.app.db.models

import com.google.gson.annotations.SerializedName
import org.ignus.app.config.INVALID_JWT_TOKEN

data class LoginResponse(@SerializedName("token") val token: String = INVALID_JWT_TOKEN)