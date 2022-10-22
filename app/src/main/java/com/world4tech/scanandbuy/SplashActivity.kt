package com.world4tech.scanandbuy

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.world4tech.scanandbuy.databinding.ActivitySplashBinding
import com.world4tech.scanandbuy.util.IntroSlider


class SplashActivity : AppCompatActivity() {
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlider(
                "Dont Know or Confused about the product name",
                R.drawable.ss1
            ),
            IntroSlider(
                "Dont Worry!! Just scan the product",
                R.drawable.ss2
            ),
            IntroSlider(
                "Choose the similar one and purchase directly",
                R.drawable.ss3
            )
        )
    )
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val pref = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = pref.getBoolean("firstStart",true)
        if (firstStart){
            binding.slideViewPager.adapter = introSliderAdapter
            setupIndicators()
            setCurrentIndicator(0)
            binding.slideViewPager.registerOnPageChangeCallback(object:
                ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            })
            binding.next.setOnClickListener {
                if (binding.slideViewPager.currentItem + 1< introSliderAdapter.itemCount){
                    binding.slideViewPager.currentItem +=1
                }else{
                    val pref = getSharedPreferences("prefs", MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putBoolean("firstStart",false)
                    editor.apply()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            binding.skipButton.setOnClickListener {
                val pref = getSharedPreferences("prefs", MODE_PRIVATE)
                val editor = pref.edit()
                editor.putBoolean("firstStart",false)
                editor.apply()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else{
            val pref = getSharedPreferences("prefs", MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("firstStart",false)
            editor.apply()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
     }
    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams :LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i] =ImageView(applicationContext)
            indicators[i].apply{
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorlayout.addView(indicators[i])
        }
    }
    private fun setCurrentIndicator(index:Int){
        val childCount = binding.indicatorlayout.childCount
        for (i in 0 until childCount){
            val imageView = binding.indicatorlayout.get(i) as ImageView
            if(i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext,R.drawable.indicator_active
                ))
            }
        }
    }
}