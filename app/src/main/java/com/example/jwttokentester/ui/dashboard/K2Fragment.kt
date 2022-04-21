package com.example.jwttokentester.ui.dashboard

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jwttokentester.K2Checking
import com.example.jwttokentester.R
import com.example.jwttokentester.databinding.FragmentK2Binding

class K2Fragment : Fragment() {

    private lateinit var k2ViewModel: K2ViewModel
    private var _binding: FragmentK2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        k2ViewModel =
            ViewModelProvider(this).get(K2ViewModel::class.java)

        _binding = FragmentK2Binding.inflate(inflater, container, false)
        val root: View = binding.root
        val str = arrayOf("Report 1", "Report 2", "Report 3", "Report 4", "Report 5")


        val assetManager = container?.context?.assets
        Log.i(ContentValues.TAG, "onCreate:  ${assetManager?.list("certs")}")
        val files = assetManager?.list("certs")
        for (file in files!!) {
            Log.i(ContentValues.TAG, "onCreate: $file")
        }

        val adp2 = ArrayAdapter(
            container.context,
            R.layout.support_simple_spinner_dropdown_item, files
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
            k2 = binding.inputK2.text.toString()
            if (k2 != "") {
                val k2decr = K2Checking(container.context).decodeK2(k2)
                Log.i(ContentValues.TAG, "onCreate\"$k2decr \\n ${k2decr.length}\",")
                k2decr.let {
                    Log.i(ContentValues.TAG, "k2 decr")
                    Toast.makeText(
                        container.context,
                        "$k2decr \n ${k2decr.length}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}