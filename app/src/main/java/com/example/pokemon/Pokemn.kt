package com.example.pokemon

import android.location.Location

class Pokemn {
    var name:String ?=null
    var des:String ?=null
    var image:Int ?=null
    var power:Double ?=null
    var location:Location ?=null
    var isCatch:Boolean ?=false
    constructor(image:Int,name:String,des:String,power:Double,lat:Double,longi:Double) {
        this.name=name
        this.des=des
        this.image=image
        this.power=power
        this.location!!.latitude=lat
        this.location=Location(name)
        this.location!!.longitude=longi

    }

}