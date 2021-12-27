package com.bazuma.myapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id:String="",
    val firstName:String="",
    val lastName:String="",
    val image:String="",
    val mobile:Long=0,
    val email:String="",
    val gender:String="",
    val profileCompleted:Int=0
): Parcelable