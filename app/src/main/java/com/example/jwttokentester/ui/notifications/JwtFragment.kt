package com.example.jwttokentester.ui.notifications

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
import com.example.jwttokentester.R
import com.example.jwttokentester.TokenChecking
import com.example.jwttokentester.databinding.FragmentJwtBinding

class JwtFragment : Fragment() {

    private lateinit var jwtViewModel: JwtViewModel
    private var _binding: FragmentJwtBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jwtViewModel =
            ViewModelProvider(this).get(JwtViewModel::class.java)

        _binding = FragmentJwtBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val assetManager = container?.context?.assets
        Log.i(ContentValues.TAG, "onCreate:  ${assetManager?.list("certs_jwt")}")
        val files = assetManager?.list("certs_jwt")
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
            k2 = binding.inputToken.text.toString()
            if (token != "") {
                if (TokenChecking(container.context).checkToken(token, certificate)) {
                    Log.i(ContentValues.TAG, "onCreate: è valido e non ci capisco più un cazzo")
                    Toast.makeText(
                        container.context,
                        "Token Valido con il certicato $certificate",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.i(ContentValues.TAG, "onCreate: porca troia")
                    Toast.makeText(
                        container.context,
                        "Token NON VALIDO con il certicato $certificate",
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