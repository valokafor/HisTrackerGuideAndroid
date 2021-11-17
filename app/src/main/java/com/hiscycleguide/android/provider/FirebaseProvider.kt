package com.hiscycleguide.android.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FirebaseProvider {

    companion object {
        private lateinit var auth : FirebaseAuth
        private lateinit var database: DatabaseReference

        private lateinit var userReference: DatabaseReference

        private lateinit var fireStoreDatabase: FirebaseFirestore
        private lateinit var userFirestore: CollectionReference

        fun newInstance() : FirebaseProvider {
            return FirebaseProvider()
        }

        fun getAuth() : FirebaseAuth {
            return auth
        }

//        fun getUserReference() : DatabaseReference {
//            return  userReference
//        }

        fun getUserFirestore() : CollectionReference {
            return  userFirestore
        }
    }

    init {
        auth = Firebase.auth
        database = Firebase.database.reference
        userReference = database.child("users")

        fireStoreDatabase = FirebaseFirestore.getInstance()
        userFirestore = fireStoreDatabase.collection("users")
    }

}