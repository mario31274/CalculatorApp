package com.example.calculatorapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.IndexOutOfBoundsException

class KeypadFragment(private val keypadPage: Int) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        if(keypadPage == 1) {
            return inflater.inflate(R.layout.fragment_keypad1, container, false)
        }
        if(keypadPage == 2) {
            return inflater.inflate(R.layout.fragment_keypad2, container, false)
        }
        Log.e("Error","Creating KeypadFragment: keypadPage $keypadPage")
        throw IndexOutOfBoundsException()
    }
    companion object {

        fun create(keypadPage: Int): KeypadFragment {
            return KeypadFragment(keypadPage)
        }
    }
}