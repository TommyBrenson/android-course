package com.example.lab07

interface DataListener {
    fun getData(data: Boolean)
    fun sendData(): Pair<Int, Int>
}