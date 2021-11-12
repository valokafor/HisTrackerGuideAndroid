package com.hiscycleguide.android.model

data class UserModel(
    var userId: String = "",
    var name: String = "",
    var email: String = "",
    var wifename: String = "",
    var mood: String = "",
    var period: Int = 28,
) {

    companion object {
        private lateinit var currentUser: UserModel

        fun setCurrentUser(user: UserModel) {
            currentUser = user
        }

        fun getCurrentUser(): UserModel {
            return currentUser
        }
    }

}