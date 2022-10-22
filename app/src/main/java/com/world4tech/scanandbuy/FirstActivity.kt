package com.world4tech.scanandbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.world4tech.scanandbuy.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {
    private lateinit var binding :ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logoImg.animate().setDuration(1500).alpha(1f).withEndAction{
            val i= Intent(this,SplashActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}