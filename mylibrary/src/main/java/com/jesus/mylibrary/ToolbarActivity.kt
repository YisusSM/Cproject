package com.jesus.mylibrary

import android.app.AppComponentFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jesus.mylibrary.interfaces.IToolbar

open class ToolbarActivity: AppCompatActivity(), IToolbar{
    protected var _toolbar: Toolbar? = null
    override fun toolbarToLoad(toolbar: Toolbar?) {
        _toolbar = toolbar
        _toolbar?.let{setSupportActionBar(_toolbar)}
    }

    override fun enabledHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }
}