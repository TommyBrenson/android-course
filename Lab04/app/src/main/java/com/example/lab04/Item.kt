package com.example.lab04

import android.os.Parcel
import android.os.Parcelable

class Item() : Parcelable {
    var kind: String = ""

    var title: String = ""

    var price: Double = 0.0

    var weight: Double = 0.0

    var manufacturer: String = ""

    val info: String
        get() = "$kind $title ($price ₽)"

    constructor(parcel: Parcel) : this() {
        kind = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
        price = parcel.readDouble()
        weight = parcel.readDouble()
        manufacturer = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kind)
        parcel.writeString(title)
        parcel.writeDouble(price)
        parcel.writeDouble(weight)
        parcel.writeString(manufacturer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}