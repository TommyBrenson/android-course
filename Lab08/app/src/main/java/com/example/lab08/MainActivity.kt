package com.example.lab08

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity(), DataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.searchButton).setOnClickListener{
            fetchRepos(50)
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_left, ReposFragment())
            .commit()
    }

    private var repos: ArrayList<Repo> = arrayListOf()

    override fun getData(): ArrayList<Repo> {
        return repos
    }

    override fun clearData() {
        repos = arrayListOf()
    }

    fun fetchRepos(limit: Int) {
        var query = findViewById<EditText>(R.id.searchInput).text.trim()
        var url = URL("https://api.github.com/search/repositories?q=$query")

        GlobalScope.launch {
            val data = url.readText()
            val JSON = JSONObject(data)
            val items = JSON.getJSONArray("items")
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val owner = item.getJSONObject("owner")
                val topics = item.getJSONArray("topics")

                val repo = Repo(
                    item.getLong("id"),
                    item.getString("name"),
                    item.getString("visibility"),
                    item.getString("description"),
                    owner.getString("login"),
                    owner.getString("avatar_url"),
                    topics.toString()
                )

                repos.add(repo)

                if (i == limit) break
            }
            MainScope().launch {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_left, ReposFragment()).commit()
            }
        }
    }

    fun JSONArray.toArrayList(): ArrayList<String> {
        val list = arrayListOf<String>()
        for (i in 0 until this.length()) {
            list.add(this.getString(i))
        }

        return list
    }
}