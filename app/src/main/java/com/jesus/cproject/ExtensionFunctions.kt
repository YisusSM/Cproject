package com.jesus.cproject

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.jesus.cproject.utils.CircleTransform
import com.squareup.picasso.Picasso
import java.util.regex.Pattern


fun Int.isNatural() = this >= 0

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Activity.toast(resourceId: Int, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, resourceId, duration).show()

fun Activity.snackBar(
    message: CharSequence,
    view: View? = findViewById(R.id.container),
    duration: Int = Snackbar.LENGTH_SHORT,
    action: String? = null,
    actionEvt: (v: View) -> Unit = {}
) {
    if (view != null) {
        val snackbar = Snackbar.make(view, message, duration)
        if (!action.isNullOrEmpty()) {
            snackbar.setAction(action, actionEvt)
        }
        snackbar.show()
    }
}

fun ViewGroup.inflate(layoutId: Int) = LayoutInflater.from(context).inflate(layoutId, this, false)!!

fun ImageView.loadByUrl(url: String) =
    Picasso.get().load(url).resize(100, 100).centerCrop().transform(CircleTransform()).into(this)

fun ImageView.loadByResource(resource: Int) = Picasso.get().load(resource).into(this)

inline fun <reified T : Activity> Activity.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent)

}

fun Activity.goToActivityResult(action: String, requesCode: Int, init: Intent.() -> Unit = {}) {
    val intent = Intent(action)
    intent.init()
    startActivityForResult(intent, requesCode)
}

fun EditText.validate(validation: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            validation(p0.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    })
}

fun Activity.isValidEmail(email: String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(email).matches()
}

fun Activity.isValidPassword(password: String): Boolean {
    // Necesita contener -->      1Numero / 1 Minuscula / 1 Mayuscula / 1 Especial / Minimo 6 Caracteres
    val passwordPattern =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$"
    val pattern = Pattern.compile(passwordPattern)
    return pattern.matcher(password).matches()
}

fun Activity.isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
    return password == confirmPassword


}

private fun Activity.addOnAdapterChangeListener(onPageChangeListener: ViewPager.OnPageChangeListener) {

}

