package com.example.lab08

interface DataListener {
    fun getData(): ArrayList<Repo>
    fun clearData()
}