package com.example.socitastic.daos

import com.example.socitastic.models.Post
import com.example.socitastic.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    private val db = FirebaseFirestore.getInstance()
    private val postCollections = db.collection("posts")
    private val auth = Firebase.auth

    fun addPost(text: String){
        val currentUserID = auth.currentUser!!.uid  //the currentUser is not null, else the app will crash

        GlobalScope.launch {
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserID).await().toObject(User::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = Post(text, user, currentTime)

            postCollections.document().set(post)
        }
    }
}