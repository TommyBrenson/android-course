package com.example.lab03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        renderView()
        addListItemListener()
    }

    val operations: Map<String, (Int, Int) -> Int> = mapOf(
                "+" to { num1, num2 -> num1 + num2 },
                "-" to { num1, num2 -> num1 - num2 },
                "*" to { num1, num2 -> num1 * num2 },
                "/" to { num1, num2 -> num1 / num2 }
    )

    private var task: Triple<String, MutableList<Int>, Int> = generateTask()
    private var correct: Int = 0
    private var incorrect: Int = 0

    fun renderView() {
        task = generateTask()
        val variants = task.second

        val titleText: TextView = findViewById(R.id.titleContainer)
        titleText.textSize = 24f
        titleText.text = task.first

        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter<Int>(this, android.R.layout.simple_list_item_1, variants)
        listView.adapter = adapter

        val scoreText: TextView = findViewById(R.id.scoreContainer)
        scoreText.textSize = 16f
        scoreText.text = getString(R.string.score_text, correct, incorrect)

    }

    fun generateTask(): Triple<String, MutableList<Int>, Int> {
        val difficulty: Int = if (correct - incorrect <= 0) 1 else ((correct - incorrect) / 10 + 1)
        var variants: MutableList<Int> = mutableListOf()

        val num1 = ((10.0.pow(difficulty - 1).toInt())..(10.0.pow(difficulty).toInt())).random()
        val num2 = ((10.0.pow(difficulty - 1).toInt())..(10.0.pow(difficulty).toInt())).random()
        val operation = operations.keys.random()

        val result = operations.getValue(operation)(num1, num2)

        for (i in 0..2) {
            variants.add(((result-10)..(result+10)).random())
        }

        variants.add(result)
        variants.shuffle()

        val expression: String = "$num1 $operation $num2 = "

        return Triple(expression, variants, result)
    }

    fun validateTask(answer: Int): Boolean {
        if (task.third == answer) {
            correct++
            return true
        } else {
            incorrect++
            return false
        }
    }

    fun addListItemListener() {
        val listView: ListView = findViewById(R.id.listView)
        listView.setOnItemClickListener{ parent, view, position, id ->
            validateTask(task.second[position])
            renderView()
        }
    }
}