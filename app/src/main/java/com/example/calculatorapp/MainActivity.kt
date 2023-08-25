package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastNumeric = false
    private var stateError = true
    private var lastDot = false

    private lateinit var expression: Expression
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onDigitClick(view: View) {
        if(stateError){
            binding.textViewQuestion.text = (view as Button).text
            stateError = false
        } else {
            binding.textViewQuestion.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }

    fun onAllClearClick(view: View) {
        binding.textViewQuestion.text = getString(R.string.question)
        binding.textViewAnswer.text = ""
        stateError = false
        lastNumeric = false
        lastDot = false
        binding.textViewAnswer.visibility = View.GONE
    }
    fun onEqualClick(view: View) {
        onEqual()
        binding.textViewQuestion.text = binding.textViewAnswer.text.toString().drop(1)
    }
    fun onClearClick(view: View) {
        binding.textViewQuestion.text = ""
        lastNumeric = false
    }
    fun onOperatorClick(view: View) {
        var op = ""
        if(!stateError && lastNumeric){
            when((view as Button).text){
                getString(R.string.button_plus) -> op = "+"
                getString(R.string.button_minus) -> op = "-"
                getString(R.string.button_multiply) -> op = "*"
                getString(R.string.button_div) -> op = "/"
                getString(R.string.button_mod) -> op = "%"
            }
            binding.textViewQuestion.append(op)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }
    fun onBackspaceClick(view: View) {
        binding.textViewQuestion.text = binding.textViewQuestion.text.toString().dropLast(1)
        try {
            val lastChar = binding.textViewQuestion.text.toString().last()
            if(lastChar.isDigit()){
                onEqual()
            }
        } catch (ex: Exception){
            binding.textViewAnswer.text = ""
            binding.textViewAnswer.visibility = View.GONE
            Log.e("last char error", ex.toString())
        }
    }

    private fun onEqual(){
        if(lastNumeric && !stateError){
            val input = binding.textViewQuestion.text.toString()

            expression = ExpressionBuilder(input).build()
            try{
                val result = expression.evaluate().toBigDecimal().toPlainString()
                binding.textViewAnswer.visibility = View.VISIBLE
                binding.textViewAnswer.text = "=$result"
            } catch (ex: ArithmeticException) {
                Log.e("evaluation error", ex.toString())
                binding.textViewAnswer.text = getString(R.string.error)
                stateError = true
                lastNumeric = false
            }
        }
    }

}