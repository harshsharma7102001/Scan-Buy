package com.world4tech.scanandbuy

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.world4tech.scanandbuy.databinding.ActivityMainBinding
import com.world4tech.scanandbuy.ml.MobilenetV110224Quant
import com.world4tech.scanandbuy.util.LoadingDialogue
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    lateinit var ivPicture:ImageView
    lateinit var tvResult:String
    lateinit var newResult:String
    lateinit var btnChoosePic: Button
    lateinit var bitmap: Bitmap
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private  var isWritePermissionGranted = false
    private var isCameraPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.newTheme)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val loading = LoadingDialogue(this)

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]?: isWritePermissionGranted
            isCameraPermissionGranted = permissions[Manifest.permission.CAMERA]?: isCameraPermissionGranted

        }
        requestPermission()
        ivPicture = binding.imageView
        btnChoosePic = binding.gallery
        // handling permissions
        btnChoosePic.setOnClickListener {
            Log.d("mssg", "button pressed")
            val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 250)
            binding.check.visibility = View.VISIBLE
        }
        binding.camera.setOnClickListener {
            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera, 200)
            binding.check.visibility = View.VISIBLE
        }
        val labels = application.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")
        binding.check.setOnClickListener {
            loading.startLoading()
            val handler = android.os.Handler()
            handler.postDelayed(object: Runnable{
                override fun run() {
                    val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
                    val model = MobilenetV110224Quant.newInstance(this@MainActivity)
                    val tbuffer = TensorImage.fromBitmap(resized)
                    val byteBuffer = tbuffer.buffer
                    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                    inputFeature0.loadBuffer(byteBuffer)
                    val outputs = model.process(inputFeature0)
                    val outputFeature0 = outputs.outputFeature0AsTensorBuffer
                    val max = getMax(outputFeature0.floatArray)
                    tvResult = labels[max]
                    model.close()
                    loading.isDismiss()
                    removespace()
                    performaction()
                }
            },5000)
        }

    }
    private fun removespace(){
        newResult = tvResult.replace("\\s".toRegex(), "_")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_header,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.about_us -> {
                val i = Intent(this@MainActivity,AboutActivity::class.java)
                startActivity(i)
                true
            }
            R.id.Privacy_policy -> {
                val i = Intent(this@MainActivity,PrivacyActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performaction() {
        val url = "https://www.amazon.com/s?k=${newResult}&crid=351W2Q14HOSM8&sprefix=${newResult}%2Caps%2C370&linkCode=ll2&tag=sillycrafter-20&linkId=30c33b690a5a667cc640eb46e63e03e1&language=en_US&ref_=as_li_ss_tl"
        Log.d("Tag",tvResult)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun requestPermission(){
        isWritePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
        val permissionRequest : MutableList<String> = ArrayList()
        if (!isWritePermissionGranted){
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!isCameraPermissionGranted){
            permissionRequest.add(Manifest.permission.CAMERA)
        }
        if (permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 250){
            ivPicture.setImageURI(data?.data)

            val uri : Uri ?= data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
        else if(requestCode == 200 && resultCode == Activity.RESULT_OK){
            bitmap = data?.extras?.get("data") as Bitmap
            ivPicture.setImageBitmap(bitmap)
        }

    }

    fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..1000)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }
}