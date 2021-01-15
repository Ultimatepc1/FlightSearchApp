package com.example.flightsearchapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView

class FlightDetailsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details_page)
        var fares = intent.getIntArrayExtra("farelist")
        var providers = intent.getStringArrayExtra("providerlist") as Array<String>
        Log.i("Provider check",providers.toString())
        var fareview = findViewById<ListView>(R.id.fareview)
        fareview.adapter = MyCustomAdapter(this,providers,fares)
    }
    private class MyCustomAdapter(context: Context,providers: Array<String>,fares: IntArray?): BaseAdapter(){
        private val mContext: Context
        private val providers: Array<String> = providers
        private val fares: IntArray? = fares
        init {
            mContext = context
        }
        override fun getItem(p0: Int): Any {
            return "Test"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return providers.size
        }
        //for each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val row = layoutInflater.inflate(R.layout.row , viewGroup, false)
            row.findViewById<TextView>(R.id.providertext).text = providers[position]
            row.findViewById<TextView>(R.id.faretext).text = "₹ " + fares!![position]
            return row
        }
    }
}