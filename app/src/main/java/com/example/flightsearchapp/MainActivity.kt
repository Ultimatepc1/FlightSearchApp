package com.example.flightsearchapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    public var airlines : HashMap<String, String>? = HashMap<String, String> ()
    public var airports : HashMap<String, String>? = HashMap<String, String> ()
    public var providers : HashMap<Integer, String>? = HashMap<Integer, String> ()
    public var flightlist : MutableList<Flight>? = mutableListOf<Flight>()
    public var days = arrayOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    public var months= arrayOf<String>(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Sept",
        "Oct",
        "Nov",
        "Dec"
    )
    private val flightAdapter: FlightAdapter = FlightAdapter(flightlist, airports, airlines,providers)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.earlyflight -> sortUpcomingFlights()
            R.id.fastestflight -> sortFastestReachingFlights()
            R.id.minfare -> sortByMinimumFare()
            R.id.maxfare -> sortByMaximumFare()
            R.id.defaultlist -> getCurrentData()
            else -> {
                Toast.makeText(applicationContext, "Some Error Ocuured", Toast.LENGTH_LONG).show()
                Log.i("Menu Error","error in menu")
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flightviewlist= findViewById(R.id.flightviewlist) as RecyclerView
        flightviewlist.layoutManager = LinearLayoutManager(this)
        flightviewlist.adapter = flightAdapter
        getCurrentData()
    }
    internal fun getCurrentData() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.mocky.io/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(FlightCall::class.java)
        val call = service.getAllFlights()
        call.enqueue(object : Callback<FlightResponse> {
            override fun onResponse(
                call: Call<FlightResponse>,
                response: Response<FlightResponse>
            ) {
                if (response.code() == 200) {
                    var result = response?.body()
                    var appendix = result?.appendix
                    var flights = result?.flights
//                    Log.i("PCInfo" ,response.body().toString())
//                    Log.i("PCInfo" , flights!![0].toString())
                    airlines = appendix?.airlines
                    airports = appendix?.airports
                    providers = appendix?.providers
                    flightlist = flights as MutableList<Flight>?
                    for(i in 0..flightlist!!.size-1){
                        flightlist!![i].fares.sortedBy { it.fare }
                    }
//                    Log.i("PCInfo", flightlist.toString())
                    changeList()
                    Toast.makeText(applicationContext, "Done", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<FlightResponse>, t: Throwable) {
                Log.e("PCError: ", t.message.toString())
                Toast.makeText(applicationContext, "Not Done", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun changeList(){
        flightAdapter.flightitems = flightlist;
        flightAdapter.airports = airports;
        flightAdapter.airlines = airlines;
        flightAdapter.providers = providers;
        flightAdapter.notifyDataSetChanged();
    }
    fun sortUpcomingFlights(){
        flightlist?.sortBy { it.departureTime }
        changeList()
    }
    fun sortFastestReachingFlights(){
        flightlist?.sortBy { it.arrivalTime }
        changeList()
    }
    fun sortByMinimumFare(){
        flightlist?.sortBy { it.fares[0].fare }
        changeList()
    }
    fun sortByMaximumFare(){
        for(i in 0..flightlist!!.size-1){
            flightlist!![i].fares.sortedByDescending { it.fare }
        }
        flightlist?.sortByDescending { it.fares[0].fare }
        changeList()
    }
}