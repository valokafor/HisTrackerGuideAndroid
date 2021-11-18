package com.hiscycleguide.android.model

import com.hiscycleguide.android.provider.PreferenceProvider

data class UserModel(
    var userId: String = "",
    var name: String = "",
    var email: String = "",
    var wifename: String = "",
    var mood: String = "",
    var period: String = "28",
    var token: String = "",
) {

    companion object {
        private lateinit var currentUser: UserModel

        fun setCurrentUser(user: UserModel) {
            PreferenceProvider.setCurrentUser(user)
            currentUser = user
        }

        fun getCurrentUser(): UserModel {
            return currentUser
        }

        fun fromPref(): UserModel? {
            var userString = PreferenceProvider.getCurrentUser() ?: return null

            userString = userString.replace("{", "")
            userString = userString.replace("}", "")
            val userJson : Map<String, String> = userString.split(",").associate {
                val (left, right) = it.split("=")
                left.trim() to right.trim()
            }

            return UserModel(
                userJson["userId"]!!,
                userJson["name"]!!,
                userJson["email"]!!,
                userJson["wifename"]!!,
                userJson["mood"]!!,
                userJson["period"]!!,
                userJson["token"]!!,
            )
        }
    }

    fun toJson(): HashMap<String, String> {
        val map = hashMapOf<String, String>()

        map["userId"] = userId
        map["name"] = name
        map["email"] = email
        map["wifename"] = wifename
        map["mood"] = mood
        map["period"] = period
        map["token"] = token

        return map
    }

}