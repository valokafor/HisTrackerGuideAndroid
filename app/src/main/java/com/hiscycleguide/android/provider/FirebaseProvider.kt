package com.hiscycleguide.android.provider

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@SuppressLint("StaticFieldLeak")
class FirebaseProvider {

    companion object {
        private lateinit var auth : FirebaseAuth

        private lateinit var fireStoreDatabase: FirebaseFirestore
        private lateinit var userFirestore: CollectionReference
        private lateinit var configFirestore: CollectionReference
        private lateinit var statusFirestore: CollectionReference
        private lateinit var insightFirestore: CollectionReference
        private lateinit var articleFirestore: CollectionReference

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

        fun getConfigFirestore() : CollectionReference {
            return  configFirestore
        }

        fun getStatusFirestore() : CollectionReference {
            return  statusFirestore
        }

        fun getInsightFirestore() : CollectionReference {
            return  insightFirestore
        }

        fun getArticleFirestore() : CollectionReference {
            return  articleFirestore
        }
    }

    init {
        auth = Firebase.auth

        fireStoreDatabase = FirebaseFirestore.getInstance()
        userFirestore = fireStoreDatabase.collection("users")
        configFirestore = fireStoreDatabase.collection("config")
        statusFirestore = fireStoreDatabase.collection("status")
        insightFirestore = fireStoreDatabase.collection("insights")
        articleFirestore = fireStoreDatabase.collection("articles")
    }

}