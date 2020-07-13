package com.jesus.cproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jesus.cproject.R
import com.jesus.cproject.activities.login.LoginActivity
import com.jesus.cproject.goToActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnGoLogin.setOnClickListener {
            goToActivity<LoginActivity>()
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
    }
}