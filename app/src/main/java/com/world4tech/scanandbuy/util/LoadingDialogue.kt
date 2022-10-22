package com.world4tech.scanandbuy.util

import android.app.Activity
import android.app.AlertDialog
import com.world4tech.scanandbuy.MainActivity
import com.world4tech.scanandbuy.R

class LoadingDialogue(val mainActivity: Activity) {
    private lateinit var isDialogue : AlertDialog
    fun startLoading(){
        val inflater = mainActivity.layoutInflater
        val dialogueView = inflater.inflate(R.layout.loading_resourse,null)
        val builder = AlertDialog.Builder(mainActivity)
        builder.setView(dialogueView)
        builder.setCancelable(false)
        isDialogue = builder.create()
        isDialogue.show()
    }
    fun isDismiss(){
        isDialogue.dismiss()
    }
}