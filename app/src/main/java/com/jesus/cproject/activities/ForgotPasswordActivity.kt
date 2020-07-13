package com.jesus.cproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.jesus.cproject.*
import com.jesus.cproject.activities.login.LoginActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "Email is NOT valid"
        }

        btnGoLogin.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }

        btnForgot.setOnClickListener {
            val email = editTextEmail.text.toString()
            if (isValidEmail(email)) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    toast("Email has been sent to reset your password")
                    goToActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } else {
                toast("Pleas make sure the email address is correct.")
            }
        }
    }
}