package com.jesus.cproject.activities


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.jesus.cproject.R
import com.jesus.cproject.adapters.PagerAdapter
import com.jesus.cproject.fragments.ChatFragment
import com.jesus.cproject.fragments.InfoFragment
import com.jesus.cproject.fragments.RatesFragment
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
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
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
}


