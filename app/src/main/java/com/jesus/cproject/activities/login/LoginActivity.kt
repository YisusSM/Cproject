package com.jesus.cproject.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jesus.cproject.*
import com.jesus.cproject.activities.ForgotPasswordActivity
import com.jesus.cproject.activities.MainActivity
import com.jesus.cproject.activities.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editTextEmail
import kotlinx.android.synthetic.main.activity_login.editTextPassword


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiClient: GoogleApiClient by lazy { getGoogleApiClient() }
    private val requestCodeGoogleSignInFlag:Int = 3001
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
        btnLoginInGoogle.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent,requestCodeGoogleSignInFlag)
        }
    }

    private fun getGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    private fun loginByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            toast("Sign In by Google!!")
        }
    }

    private fun loginByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        if (mAuth.currentUser!!.isEmailVerified) {
                            // Sign in success, update UI with the signed-in user's information
                            toast("Sign In Success!!")
                            goToActivity<MainActivity>()
                            overridePendingTransition(
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right
                            )
                        } else {
                            toast("User must confirm email first")
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        toast("Email Or Password Incorrect.")
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data!!)
        if (requestCode == requestCodeGoogleSignInFlag){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result!!.isSuccess){
                val account = result!!.signInAccount
                loginByGoogleAccountIntoFirebase(account!!)
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast("connection failed!!")
    }

}