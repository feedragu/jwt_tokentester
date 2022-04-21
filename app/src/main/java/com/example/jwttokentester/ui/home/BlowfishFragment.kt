package com.example.jwttokentester.ui.home

import android.R
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jwttokentester.crypto.BlowFish
import com.example.jwttokentester.crypto.BlowFish.TAG
import com.example.jwttokentester.crypto.K2Utils
import com.example.jwttokentester.databinding.FragmentBlowfishBinding

class BlowfishFragment : Fragment() {

    private lateinit var blowfishViewModel: BlowfishViewModel
    private var _binding: FragmentBlowfishBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        blowfishViewModel =
            ViewModelProvider(this).get(BlowfishViewModel::class.java)

        _binding = FragmentBlowfishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inputK2: TextView = binding.inputK2

        val matricolaInput: TextView = binding.matricolaInput

        val pop: TextView = binding.inputPop


        val assetManager = container?.context?.assets
        Log.i(ContentValues.TAG, "onCreate:  ${assetManager?.list("certs")}")
        val files = assetManager?.list("certs")
        for (file in files!!) {
            Log.i(ContentValues.TAG, "onCreate: $file")
        }

        val adp2 = ArrayAdapter(
            container.context,
            R.layout.simple_spinner_dropdown_item, files
        )


        binding.spinnerCert.prompt = "Seleziona Certificato"
        binding.spinnerCert.adapter = adp2


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
                Log.i(ContentValues.TAG, "onItemSelected: ${files.get(position)}")
                certificate = files.get(position)
            }

        }

        binding.checkToken.setOnClickListener { view: View ->
            try {
                val k2decrypted: String =
                    K2Utils.retrieveK2(
                        inputK2.text.toString(),
                        matricolaInput.text.toString(),
                        pop.text.toString()
                    )
                Log.i(
                    ContentValues.TAG,
                    "k2 decr \n ${inputK2.text} \n ${matricolaInput.text} \n ${pop.text}"
                )
                Toast.makeText(
                    container.context,
                    "$k2decrypted \n ${k2decrypted.length}",
                    Toast.LENGTH_LONG
                ).show()

                if (BlowFish.getErrorList().contains(k2decrypted)) {
                    Log.i(TAG, "onCreateView: fuck")
                }
            } catch (e: Exception) {
                Log.i(TAG, "onCreateView: ${e.printStackTrace()}")
            }


        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}