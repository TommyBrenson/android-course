package com.example.lab01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun initNumbers(): Pair<Float, Float> {
        val edit1: EditText = findViewById(R.id.number1)
        val edit2: EditText = findViewById(R.id.number2)

        val n1 = edit1.text.toString().toFloat()
        val n2 = edit2.text.toString().toFloat()

        return Pair(n1, n2)
    }

    private fun printResult(result: Float) {
        val textView: TextView = findViewById(R.id.result)
        val resText = resources.getString(R.string.operation_result)
        textView.text = String.format(resText, result)
    }

    fun addButtonClick(view: View) {
        val (n1, n2) = initNumbers()
        val n = n1 + n2
        printResult(n)
    }
    fun subtractButtonClick(view: View) {
        val (n1, n2) = initNumbers()
        val n = n1 - n2
        printResult(n)
    }
    fun multiplyButtonClick(view: View) {
        val (n1, n2) = initNumbers()
        val n = n1 * n2
        printResult(n)
    }
    fun divideButtonClick(view: View) {
        val (n1, n2) = initNumbers()
        val n = n1 / n2
        printResult(n)
    }
}