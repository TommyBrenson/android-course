package com.example.lab08

import android.os.Parcel
import android.os.Parcelable

class Repo (
        var id: Long = 0L,
        var title: String = "",
        var visibility: String = "",
        var description: String = "",
        var userName: String = "",
        var userAvatar: String = "",
        var topics: String = ""
    )
{
    override fun toString(): String {
        return title
    }
}