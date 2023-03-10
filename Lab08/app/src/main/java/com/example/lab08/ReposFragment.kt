package com.example.lab08

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class ReposFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_left, container, false)
        renderView(view)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
        repos = (mainContext as DataListener).getData()
        (mainContext as DataListener).clearData()
    }

    private lateinit var mainContext: Context
    private var repos: ArrayList<Repo> = arrayListOf()

    fun renderView(parentView: View) {
        val listOptions = parentView.findViewById<ListView>(R.id.reposList)
        //Log.i("REPOS", repos.toString())
        listOptions.adapter = ArrayAdapter<Repo>(requireContext(),
        android.R.layout.simple_list_item_1, repos)
        listOptions.setOnItemClickListener{ parent, view, position, id ->
            val repo = repos[position]

            val repoInfoFragment = RepoInfoFragment()
            val bundle = Bundle()

            Log.i("REPOS", repo.userAvatar)
            bundle.putLong("id", repo.id)
            bundle.putString("title", repo.title)
            bundle.putString("visibility", repo.visibility)
            bundle.putString("description", repo.description)
            bundle.putString("username", repo.userName)
            bundle.putString("avatar", repo.userAvatar)
            bundle.putString("topics", repo.topics)

            repoInfoFragment.arguments = bundle

            fragmentManager?.beginTransaction()?.replace(R.id.fragment_right, repoInfoFragment)?.commit()
        }
    }
}