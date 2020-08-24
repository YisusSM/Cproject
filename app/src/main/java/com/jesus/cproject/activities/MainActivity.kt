package com.jesus.cproject.activities


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.jesus.cproject.R
import com.jesus.cproject.activities.login.LoginActivity
import com.jesus.cproject.adapters.PagerAdapter
import com.jesus.cproject.fragments.ChatFragment
import com.jesus.cproject.fragments.InfoFragment
import com.jesus.cproject.fragments.RatesFragment
import com.jesus.cproject.goToActivity
import com.jesus.mylibrary.ToolbarActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {

    private var prevBottomSelected: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarToLoad(toolbarView as Toolbar)
        setUpViewPager(getPagerAdapter())
        setAppBottonNavigationBar()

    }

    private fun getPagerAdapter(): PagerAdapter {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        adapter.addFragment(ChatFragment())
        return adapter
    }

    private fun setUpViewPager(adapter: PagerAdapter) {
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = adapter.count
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (prevBottomSelected == null) {
                    btnNavigation.menu.getItem(0).isChecked = false
                } else {
                    prevBottomSelected!!.isChecked = false
                }
                btnNavigation.menu.getItem(position).isChecked = true
                prevBottomSelected = btnNavigation.menu.getItem(position)

            }
        })

    }

    private fun setAppBottonNavigationBar() {
        btnNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btn_nav_info -> {
                    viewPager.currentItem = 0;true
                }
                R.id.btn_nave_rates -> {
                    viewPager.currentItem = 1;true
                }
                R.id.btn_nav_chat -> {
                    viewPager.currentItem = 2;true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.general_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_log_out -> {
                FirebaseAuth.getInstance().signOut()
                goToActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


