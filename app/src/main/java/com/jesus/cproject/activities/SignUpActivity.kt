package com.jesus.cproject.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.jesus.cproject.R
import com.jesus.cproject.activities.login.LoginActivity
import com.jesus.cproject.goToActivity
import com.jesus.cproject.toast
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btnGoLogin.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        btnSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (isValidEmailPassword(email, password)) {
                signUpByEmail(email, password)
            } else {
                toast("Confirma si la contraseña es correcta")
            }
        }
    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                toast("Un correo se te ha enviado. porfavor confirma.")

            } else {
                // If sign in fails, display a message to the user.
                toast("Ocurrio un error inesperado. porfavor intentelo de nuevo.")

            }


        }
    }

    private fun isValidEmailPassword(email: String, password: String): Boolean {
        return !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                password == editTextConfirmPassword.text.toString()
    }
}