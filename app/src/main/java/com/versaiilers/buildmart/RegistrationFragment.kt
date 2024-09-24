package com.versaiilers.buildmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class RegistrationFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_registration, container, false)

        view.findViewById<Button>(R.id.fragment_registration_button_go_back).setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_registrationFragment_to_mainAuthFragment)
        }
        return view
    }

}