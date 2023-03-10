package com.example.lab02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            val array = savedInstanceState.getIntegerArrayList("intArray")
            if (array != null) {
                list = array
                for (i in 0..array.size - 1) {
                    addTextView(array[i])
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("MyInfo", "Метод onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MyInfo", "Метод onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MyInfo", "Метод onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MyInfo", "Метод onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MyInfo", "Метод onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("intArray", list)
//        scrollY = getScrollView().scrollY
//        outState.putInt("scrollY", scrollY)
    }

    private var viewsCount: Int = 0
    private var list: ArrayList<Int> = ArrayList<Int>()
    private var scrollY: Int = 0

    fun buttonAddClick(view: View) {
        addTextView()
    }

    fun initTextView(number: Int) {
        val textView = TextView(this)

        textView.text = number.toString()
        textView.textSize = 24f
        val container = findViewById<LinearLayout>(R.id.innerContainer)
        container.addView(textView)
    }

    fun addTextView() {
        val textView = TextView(this)
        val number = (0..10).random()
        list.add(number)
        initTextView(number)
    }

    fun addTextView(number: Int) {
        initTextView(number)
    }

}