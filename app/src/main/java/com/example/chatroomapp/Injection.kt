package com.example.chatroomapp

import com.google.firebase.firestore.FirebaseFirestore


// This Object is used to create instance of FirebaseFirestore

object Injection{
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    fun instance(): FirebaseFirestore{
        return instance
    }

}