package com.jesus.cproject.activities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.jesus.cproject.*
import com.jesus.cproject.activities.ForgotPasswordActivity
import com.jesus.cproject.activities.MainActivity
import com.jesus.cproject.activities.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editTextEmail
import kotlinx.android.synthetic.main.activity_login.editTextPassword



class LoginActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password)) {
                loginByEmail(email, password)
            } else toast("Llena todos los campos.")
        }
        btnCreateAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        textViewForgotPassword.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "El email is NOT valid"
        }
        editTextPassword.validate {
            editTextPassword.error =
                if (isValidPassword(it)) null else "El password should contain 1 lower case, 1 uppercase, 1 number, 1 special character and  6 character lenght at least"
        }
    }

    private fun loginByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        toast("Sign In Success!!")
                        val currentUser = mAuth.currentUser!!
                        currentUser.displayName
                        currentUser.email
                        currentUser.photoUrl
                        currentUser.phoneNumber
                        currentUser.isEmailVerified
                        goToActivity<MainActivity>()
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                    } else {
                        // If sign in fails, display a message to the user.
                        toast("Email Or Password Incorrect.")
                    }
                })
    }

}