package com.example.myapplication

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addCells(findViewById(R.id.calculator_input_container_line1), 0)
        addCells(findViewById(R.id.calculator_input_container_line2), 1)
        addCells(findViewById(R.id.calculator_input_container_line3), 2)
        addCells(findViewById(R.id.calculator_input_container_line4), 3)
        addCells(findViewById(R.id.calculator_input_container_line5), 4)
    }


    companion object {
        private val INPUT_BUTTONS = listOf(
                listOf("", "","C", "CE"),
                listOf("1", "2", "3", "/"),
                listOf("4", "5", "6", "*"),
                listOf("7", "8", "9", "-"),
                listOf("0", ".", "=", "+")
        )
    }

    private fun addCells(linearLayout: LinearLayout, position: Int) {
        for (x in INPUT_BUTTONS[position].indices) {
            linearLayout.addView(
                    TextView(
                            ContextThemeWrapper(this, R.style.CalculatorInputButton)
                    ).apply {
                        text = INPUT_BUTTONS[position][x]
                        setOnClickListener { onCellClicked(this.text.toString()) }
                    },
                    LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                    )
            )
        }
    }


    private var input: Float? = null
    private var previousInput: Float? = null
    private var symbol: String? = null
    private var result: Float? = null

    fun String.isNum(): Boolean {
        return length == 1 && isDigitsOnly()
    }

    private fun onSymbolClicked(symbol: String) {
        if(input == null && result == null){
            return
        }
        if(input == null){
            this.symbol = symbol
            previousInput = result
            result = null
            return
        }
        this.symbol = symbol
        previousInput = input
        input = null
    }

    private fun onCellClicked(value: String) {

        fun updateDisplayContainer(value: Any) {
            findViewById<TextView>(R.id.calculator_display_container).text = value.toString()
        }

        fun onClearClicked() {
            if(input == null){
               return
            }

            if(symbol == null){
                // input = input.toString().dropLast(1).toFloat()
                input = null
                updateDisplayContainer("")
                return
            }

            // previousInput = previousInput.toString().dropLast(1).toFloat()
            input = null
            updateDisplayContainer(previousInput.toString())
            return
        }

        fun onEqualsClicked() {
            if (input == null || previousInput == null || symbol == null) {
                return
            }
            if (input == 0.0f && symbol == "/") {
                updateDisplayContainer("Cannot divide by 0")
                return
            }

            when (symbol) {
                "+" -> {
                    result = previousInput!! + input!!
                    updateDisplayContainer(result.toString())
                }
                "-" -> {
                    result = previousInput!! - input!!
                    updateDisplayContainer(result.toString())
                }
                "*" -> {
                    result = previousInput!! * input!!
                    updateDisplayContainer(result.toString())
                }
                "/" -> {
                    result = previousInput!! / input!!
                    updateDisplayContainer(result.toString())
                }
                else -> {
                    updateDisplayContainer("ERROR")
                }
            }

            input = null
            previousInput = null
            symbol = null
        }

        when {
            value.isNum() -> {
                if(result != null){
                    result = null
                }
                input = value.toFloat()
                updateDisplayContainer(value)
            }
            value == "=" -> onEqualsClicked()

            listOf("/", "*", "-", "+").contains(value) -> onSymbolClicked(value)

            value == "CE" -> {
                result = null
                updateDisplayContainer("")
            }
            value == "C" -> onClearClicked()
        }
    }
}