package com.example.lab05

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private var items = ArrayList<Item>()
    private lateinit var con: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.listItems)
        listView.adapter = ItemAdapter(this, items)

        listView.setOnItemClickListener { adapterView: AdapterView<*>, view: View, i: Int, l: Long ->
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("index", i)
            intent.putExtra("item", items[i])

            startActivityForResult(intent, 0)
        }

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("add", true)
            startActivityForResult(intent, 0)
        }

        val db = SQLiteHelper(this)
        con = db.readableDatabase
        getItems()
    }

    fun getItems() {
        val cursor = con.query("items",
            arrayOf("id", "kind", "title", "price", "weight", "manufacturer", "photo"),
            null, null, null, null, null
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val s = Item()
            s.id = cursor.getInt(0)
            s.kind = cursor.getString(1)
            s.title = cursor.getString(2)
            s.price = cursor.getDouble(3)
            s.weight = cursor.getDouble(4)
            s.manufacturer = cursor.getString(5)
            s.photo = cursor.getString(6)
            items.add(s)
            cursor.moveToNext()
        }
        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val index: Int = data?.getIntExtra("index", -1) ?: -1
            val item: Item = data?.getParcelableExtra("item") ?: Item()

            val cv = ContentValues()
            cv.put("kind", item.kind)
            cv.put("title", item.title)
            cv.put("price", item.price)
            cv.put("weight", item.weight)
            cv.put("manufacturer", item.manufacturer)
            cv.put("photo", item.photo)

            when(index) {
                (-1) -> {
                    items.add(item)
                    con.insert("items", null, cv)
                }
                (-2) -> {
                    items.removeIf {it.id == item.id}
                }
                else -> {
                    items[index] = item
                    cv.put("id", item.id)
                    con.update("items", cv, "id=?", arrayOf(item.id.toString()))
                }
            }

            val listView: ListView = findViewById(R.id.listItems)
            (listView.adapter as ItemAdapter).notifyDataSetChanged()
        }
    }
}