package com.hiscycleguide.android.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseProvider {

    companion object {
        private lateinit var auth : FirebaseAuth
        private lateinit var database: DatabaseReference

        private lateinit var userReference: DatabaseReference

        fun newInstance() : FirebaseProvider {
            return FirebaseProvider()
        }

        fun getAuth() : FirebaseAuth {
            return auth
        }

        fun getUserReference() : DatabaseReference {
            return  userReference
        }
    }

    init {
        auth = Firebase.auth
        database = Firebase.database.reference
        userReference = database.child("users")
    }

}