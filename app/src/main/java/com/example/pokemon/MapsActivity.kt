package com.example.pokemon

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.pokemon.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
        loadPokemon()

    }
    var accessLocation=123
    fun checkPermission(){
     //   if (Build.VERSION.SDK_INT >=23){
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),accessLocation)
            }
        //}
        getUserLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            accessLocation->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }else{
                    Toast.makeText(this,"mlocation access is deny",Toast.LENGTH_SHORT).show()

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    fun getUserLocation(){
        Toast.makeText(this,"mlocation access now",Toast.LENGTH_SHORT).show()
        val myLocation=MyLocation()
        val locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

        val myThread=MyThread()
        myThread.start()

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions()
            .position(sydney)
            .snippet("here is my mlocation")
            .title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.img)))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    var mlocation:Location?=null
    var oldLocation:Location?=null
    inner class MyLocation:LocationListener{
        constructor(){
            oldLocation= Location("oldlocation")
            oldLocation!!.longitude=0.0
            oldLocation!!.latitude=0.0

        }
        override fun onLocationChanged(locationP: Location) {

            mlocation=locationP

        }
    }

    inner class MyThread: Thread() {


        override fun run() {
            while (true){
                try {

                    if (mlocation?.let { oldLocation!!.distanceTo(it) } ==0f){
                        continue
                    }
                    oldLocation=mlocation

                runOnUiThread {
                    mMap.clear()

                    // Add a marker in Sydney and move the camera
                    val sydney = LatLng(mlocation!!.latitude, mlocation!!.longitude)
                    mMap.addMarker(MarkerOptions()
                        .position(sydney)
                        .snippet("here is my mlocation")
                        .title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.img)))

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

                    //show pokemons
                    for (i in 0.. listOfPokemon.size-1){
                     var newPokemon=listOfPokemon[i]

                     if (newPokemon.isCatch==false){
                         val poke1 = LatLng(newPokemon.location!!.latitude,newPokemon.location!!.longitude)
                         mMap.addMarker(MarkerOptions()
                             .position(poke1)
                             .snippet(newPokemon.des)
                             .title("pokemon 1").icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))

                         if (mlocation!!.distanceTo(newPokemon.location!!)<2){
                             myPower=myPower+newPokemon.power!!
                             newPokemon.isCatch=true
                             listOfPokemon[i]=newPokemon
                             Toast.makeText(applicationContext,"you catch new pokemon",Toast.LENGTH_SHORT).show()
                         }
                     }
                    }

                }
                    sleep(1000)
                }catch (ex:java.lang.Exception){
                    println("$ex")
                }
            }
        }
    }

    var myPower:Double=0.0


    var listOfPokemon=ArrayList<Pokemn>()
    fun loadPokemon(){
        listOfPokemon.add(Pokemn(R.drawable.img_1,"Pokemon 1","pkemon1 living here In Train Station ",55.0,31.6297,-8.019))
        listOfPokemon.add(Pokemn(R.drawable.img_2,"Pokemon 2","living here In Marjane Agadir",95.0, 31.6294,  -8.0797))
        listOfPokemon.add(Pokemn(R.drawable.img_3,"Pokemon 3"," living here In Marjane Casa",33.0,31.6292,-8.079))
    }
}