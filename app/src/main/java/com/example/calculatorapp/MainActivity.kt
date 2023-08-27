package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception
import java.util.EmptyStackException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var keypadPager: ViewPager2
    private var lastNumeric = false
    private var stateError = true
    private var lastDot = false

    private lateinit var expression: Expression
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        keypadPager = binding.keypadPager
        keypadPager.adapter = ViewPagerAdapter(this)
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

    fun onConstantClick(view: View) {
        val const: String = when ((view as Button).text) {
            getString(R.string.button_pi) -> "pi"
            else -> view.text.toString()
        }
        if(stateError) {
            binding.textViewQuestion.text = const
            stateError = false
        } else {
            binding.textViewQuestion.append(const)
        }
        lastNumeric = true
        onEqual()
    }

    fun onDotClick(view: View) {
        if(stateError) {
            binding.textViewQuestion.text = (view as Button).text
        } else if(!lastDot){
            binding.textViewQuestion.append((view as Button).text)
        }
        stateError = false
        lastDot = true
        onEqual()
    }

    fun onAllClearClick(view: View) {
        binding.textViewQuestion.text = getString(R.string.question)
        binding.textViewAnswer.text = ""
        stateError = true
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
        if(!stateError && lastNumeric){
            val op: String = when((view as Button).text){
                getString(R.string.button_multiply) -> "*"
                getString(R.string.button_div) -> "/"
                getString(R.string.button_power) -> "^"
                else -> (view.text.toString())
            }
            binding.textViewQuestion.append(op)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun onFunctionClick(view: View) {
        val op: String = when((view as Button).text.toString()){
            getString(R.string.button_sqrt) -> "sqrt"
            getString(R.string.button_log) -> "log"
            else -> (view.text.toString())
        }
        if(stateError) {
            binding.textViewQuestion.text = op
            stateError = false
        } else {
            binding.textViewQuestion.append(op)
        }
        binding.textViewQuestion.append("(")
    }

    fun onBackspaceClick(view: View) {
        binding.textViewQuestion.text = binding.textViewQuestion.text.toString().dropLast(1)
        try {
            val lastChar = binding.textViewQuestion.text.toString().last()
            if(lastChar.isDigit()){
                lastNumeric = true
                lastDot = false
                onEqual()
            } else if(lastChar == '.') {
                lastDot = true
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

            try {
                expression = ExpressionBuilder(input).build()
                try{
                    val result = expression.evaluate().toBigDecimal().toPlainString()
                    binding.textViewAnswer.visibility = View.VISIBLE
                    binding.textViewAnswer.text = "=$result"
                } catch (ex: ArithmeticException) {
                    Log.e("evaluation error", ex.toString())
                    binding.textViewAnswer.visibility = View.VISIBLE
                    binding.textViewAnswer.text = getString(R.string.error)
                    stateError = true
                    lastNumeric = false
                } catch (ex: IllegalArgumentException) {
                    Log.e("Illegal Argument error", ex.toString())
                    binding.textViewAnswer.visibility = View.VISIBLE
                    binding.textViewAnswer.text = getString(R.string.error)
                }
            } catch (ex: IllegalArgumentException) {
                Log.e("Parenthesis error", ex.toString())
            } catch (ex: EmptyStackException) {
                Log.e("Empty Parenthesis error", ex.toString())
                binding.textViewAnswer.visibility = View.VISIBLE
                binding.textViewAnswer.text = getString(R.string.error)
                stateError = true
                lastNumeric = false
            }
        }
    }

}