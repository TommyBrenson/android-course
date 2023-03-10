package com.example.lab08

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.URL

class RepoInfoFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_right, container, false)
        val args = this.arguments
        if (args != null) {
            repo.id = args.getLong("id")
            repo.title = args.getString("title").toString()
            repo.visibility = args.getString("visibility").toString()
            repo.description = args.getString("description").toString()
            repo.userName = args.getString("username").toString()
            repo.userAvatar = args.getString("avatar").toString()
            repo.topics = args.getString("topics").toString()
        }

        renderView(view)

        return view
    }

    private var repo = Repo()

    @SuppressLint("SetTextI18n")
    fun renderView(parentView: View) {

        GlobalScope.launch {
            var imgLoader = Picasso.get().load(repo.userAvatar)
            MainScope().launch {
                var topics = repo.topics.replace(Regex("[\\[\\]\"]"), "")
                var imgView = parentView.findViewById<ImageView>(R.id.userAvatar)
                imgLoader.into(imgView)
                parentView.findViewById<TextView>(R.id.userName).text = repo.userName
                parentView.findViewById<TextView>(R.id.repoTitle).text = repo.title
                parentView.findViewById<TextView>(R.id.repoDescription).text = "Description: ${repo.description}"
                parentView.findViewById<TextView>(R.id.repoVisibility).text = "Visibility: ${repo.visibility}"
                parentView.findViewById<TextView>(R.id.repoTopics).text = "Topics: ${topics}"
            }
        }
    }
}