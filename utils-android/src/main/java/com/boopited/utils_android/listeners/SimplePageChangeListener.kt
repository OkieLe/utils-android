package com.boopited.utils_android.listeners

import androidx.viewpager.widget.ViewPager

interface SimplePageChangeListener: ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
        //Implement if need
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //Implement if need
    }

    override fun onPageSelected(position: Int) {
        //Implement if need
    }
}