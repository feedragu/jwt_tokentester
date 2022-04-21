package com.example.jwttokentester

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.jwttokentester.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val str = arrayOf("Report 1", "Report 2", "Report 3", "Report 4", "Report 5")

        val assetManager = assets
        Log.i(TAG, "onCreate:  ${assetManager.list("certs")}")
        val files = assetManager.list("certs")
        for (file in files!!) {
            Log.i(TAG, "onCreate: $file")
        }

        val adp2 = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, files
        )


        binding.spinnerCert.prompt = "Seleziona Certificato"
        binding.spinnerCert.adapter = adp2

        binding.videoRecon.setOnClickListener { view: View ->
            val intent = Intent(this, CameraActivity::class.java)
            resultLauncher.launch(intent)
        }

        var token = ""
        var k2 = ""
        var certificate = files.first()

        binding.spinnerCert.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.i(TAG, "onItemSelected: ${files.get(position)}")
                certificate = files.get(position)
            }

        }

        binding.checkToken.setOnClickListener { view: View ->
//            token = binding.inputToken.text.toString()
            k2 = binding.inputK2.text.toString()
            if (token != "") {
                if (TokenChecking(this).checkToken(token, certificate)) {
                    Log.i(TAG, "onCreate: è valido e non ci capisco più un cazzo")
                    Toast.makeText(
                        this,
                        "Token Valido con il certicato $certificate",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.i(TAG, "onCreate: porca troia")
                    Toast.makeText(
                        this,
                        "Token NON VALIDO con il certicato $certificate",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (k2 != "") {
                try {
                    val k2decr = K2Checking(this).decodeK2(k2)
                    Log.i(TAG, "onCreate:                         \"$k2decr \\n ${k2decr.length}\",")
                    k2decr.let {
                        Log.i(TAG, "k2 decr")
                        Toast.makeText(
                            this,
                            "$k2decr \n ${k2decr.length}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }catch (e : SecurityException) {
                    Toast.makeText(
                        this,
                        "Errore nella decriptazione della chiave K2 con chiave ${files.get(binding.spinnerCert.selectedItemPosition)}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        }

    }
}