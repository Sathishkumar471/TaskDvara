package com.example.taskdvara

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.taskdvara.DBClass
import androidx.annotation.RequiresApi
import android.os.Build
import android.os.Bundle
import com.example.taskdvara.R
import android.widget.Toast
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.taskdvara.Globals
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.auth.api.credentials.Credential.EXTRA_KEY
import com.google.android.gms.auth.api.credentials.Credentials.getClient
import com.google.android.gms.auth.api.credentials.HintRequest

class MainActivity : AppCompatActivity() {
    private lateinit var speed: TextView
    private lateinit var txt_date: TextView
    private lateinit var phonenumber: TextView
    private lateinit var btn_upload: Button
    var db: DBClass? = null
    var down : Double? = null
    var up : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speed = findViewById(R.id.speed)
        txt_date = findViewById(R.id.date)
        phonenumber = findViewById(R.id.phonenumber)
        btn_upload = findViewById(R.id.btn_upload)
        db = DBClass(this@MainActivity)
        dB
        checkInternet()
        requestHint()
        val yourmilliseconds = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultdate = Date(yourmilliseconds)
        println(sdf.format(resultdate))

        txt_date.setText(sdf.format(resultdate))
        btn_upload.setOnClickListener{ saveData() }
    }

    private fun requestHint() {

        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intent = getClient(this).getHintPickerIntent(hintRequest)
        val intentSender = IntentSenderRequest.Builder(intent.intentSender).build()

        val resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val credential:com.google.android.gms.auth.api.credentials.Credential? = result.data?.getParcelableExtra(EXTRA_KEY)
                // Phone number with country code
                // (run { credential?.id }).also { number }
                Log.i("mTag", "Selected phone No: ${credential?.id}")
                phonenumber.setText(credential?.id)
            }
        }
        resultLauncher.launch(intentSender)
    }

    private fun saveData() {
        db!!.Insert_User(
            phonenumber!!.text.toString(),
            speed!!.text.toString(),
            txt_date!!.text.toString()
        )
        Toast.makeText(
            this,
            resources.getString(R.string.insert), Toast.LENGTH_LONG
        ).show()
        val intent = Intent(this@MainActivity, ListActivity::class.java)
        startActivity(intent)
        finish()
    }

    val dB: Unit
        get() {
            db!!.openToWrite()
        }

   /* override fun onPause() {
        super.onPause()
        db!!.closeDB()
    }*/

    private fun checkInternet() {
        if (Globals.isConnected(this@MainActivity)) {
            val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val nc = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val downSpeed = nc!!.linkDownstreamBandwidthKbps
            val upSpeed = nc.linkUpstreamBandwidthKbps

            down = (downSpeed/1000).toDouble()
            up = (upSpeed/1000).toDouble()

            speed.setText("${down} mbps / ${up} mbps")

            System.err.println("speed " + down + " " + up)
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.check_internet_connection_try_again), Toast.LENGTH_LONG
            ).show()
        }
    }
}