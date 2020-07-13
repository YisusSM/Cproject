package com.jesus.cproject.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.jesus.cproject.*
import com.jesus.cproject.activities.login.LoginActivity
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
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        btnSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(
                    password,
                    confirmPassword
                )
            ) {
                signUpByEmail(email, password)
            } else {
                toast("Asegurate de que todos los datos sean correctos")
            }
        }

        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "El email is NOT valid"
        }
        editTextPassword.validate {
            editTextPassword.error =
                if (isValidPassword(it)) null else "El password should contain 1 lower case, 1 uppercase, 1 number, 1 special character and  6 character lenght at least"
        }
        editTextConfirmPassword.validate {
            editTextConfirmPassword.error = if (isValidConfirmPassword(
                    editTextPassword.text.toString(),
                    it
                )
            ) null else "Confirm password does not match with Password"
        }
    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener {
                    // Sign in success, update UI with the signed-in user's information
                    toast("Un correo se te ha enviado. porfavor confirma.")
                    goToActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }
            } else {
                // If sign in fails, display a message to the user.
                toast("Ocurrio un error inesperado. porfavor intentelo de nuevo.")

            }


        }
    }


}