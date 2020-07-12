package com.jesus.cproject.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.jesus.cproject.R
import com.jesus.cproject.toast


class SignUpActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            toast("User is NOT logged in")
            createAccount("yisus2@gmail.com","123456789")
        } else {
            toast("user IS logged in")

        }

    }

    private fun createAccount(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    toast("createUserWithEmail:success")
                    val user = mAuth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    toast("createUserWithEmail:failure")

                }
            }
    }
}