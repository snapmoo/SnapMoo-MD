package com.bangkit.snapmoo.ui.home.udder

import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.data.local.ImageData
import com.bangkit.snapmoo.databinding.ActivityUdderBinding
import com.bangkit.snapmoo.ui.adapter.ImageSlideAdapter

class UdderActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUdderBinding
    private lateinit var adapter: ImageSlideAdapter
    private val list = ArrayList<ImageData>()
    private lateinit var dots: ArrayList<TextView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUdderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        imageSlide()
    }


    private fun imageSlide() {
        list.add(
            ImageData(
                R.drawable.udder_1
            )
        )
        list.add(
            ImageData(
                R.drawable.udder_2
            )
        )
        list.add(
            ImageData(
                R.drawable.udder_3
            )
        )
        list.add(
            ImageData(
                R.drawable.udder_4
            )
        )
        list.add(
            ImageData(
                R.drawable.udder_5
            )
        )


        adapter = ImageSlideAdapter(list)
        binding.viewPager.adapter = adapter
        dots = ArrayList()
        setIndicator()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                selectedDot(position)
                super.onPageSelected(position)
            }
        })
    }
    private fun selectedDot(position : Int){
        for (i in 0 until list.size){
            if (i == position)
                dots[i].setTextColor(ContextCompat.getColor(this, R.color.blue_600))
            else
                dots[i].setTextColor(ContextCompat.getColor(this, R.color.silver_500))
        }
    }

    private fun setIndicator() {
        for(i in 0 until list.size){
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            dots[i].textSize = 18f
            binding.indicator.addView(dots[i])
        }
    }

    private fun setToolbar() {
        binding.toolbar.btnBackToolbar.setOnClickListener {
            onBackPressed()
        }
        val label = getActivityLabel()
        binding.toolbar.tvToolbarTitle.text = label
    }

    private fun getActivityLabel(): String {
        return try {
            val activityInfo = packageManager.getActivityInfo(componentName, 0)
            activityInfo.loadLabel(packageManager).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            title.toString()  // default title
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back_toolbar -> {
                finish()
            }
        }
    }
}