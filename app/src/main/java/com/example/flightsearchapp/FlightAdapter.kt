package com.example.flightsearchapp

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class FlightAdapter(var flightitems: MutableList<Flight>?,var airports: HashMap<String, String>?,var airlines: HashMap<String, String>?,var providers: HashMap<Integer, String>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class FlightViewHolder(itemView: View,airports: HashMap<String, String>?,airlines: HashMap<String, String>?,var providers: HashMap<Integer, String>?): RecyclerView.ViewHolder(itemView){
        fun bind(flightModel: Flight,airports: HashMap<String, String>?,airlines: HashMap<String, String>?,providers: HashMap<Integer, String>?){
            itemView.findViewById<TextView>(R.id.combolocation).text = flightModel.originCode+" - "+flightModel.destinationCode;
            itemView.findViewById<TextView>(R.id.airport1name).text = airports!!.get(
                flightModel.originCode
            )+" International Airport";
            itemView.findViewById<TextView>(R.id.airport2name).text = airports!!.get(
                flightModel.destinationCode
            )+" International Airport";
            itemView.findViewById<TextView>(R.id.flightclass).text = "Non Stop | "+flightModel.`class`+" class"
            itemView.findViewById<TextView>(R.id.flightairline).text = airlines!!.get(
                flightModel.airlineCode
            )
            val adate= flightModel.departureTime
            var (adays,atime) = getdate(adate)
            itemView.findViewById<TextView>(R.id.departingdate).text = adays
            itemView.findViewById<TextView>(R.id.arrival_Txt).text = atime
            val ddate= flightModel.arrivalTime
            var (ddays,dtime) = getdate(ddate)
            itemView.findViewById<TextView>(R.id.landingdate).text = ddays
            itemView.findViewById<TextView>(R.id.depart_txt).text = dtime
            itemView.findViewById<TextView>(R.id.landingdate2).text = ddays
            var diff = ((ddate-adate)/(1000*60*60)).toString()+" hrs"
            itemView.findViewById<TextView>(R.id.hour_txt).text = diff
            var fares1 = mutableListOf<Int>()
            var provider1 = mutableListOf<String?>()
            for(i in flightModel.fares.indices){
                fares1.add(flightModel.fares[i].fare)
                provider1.add(providers?.get(flightModel.fares[i].providerId))
                Log.i("Pc provider", provider1.get(i).toString())
            }
            var fares = fares1.toIntArray()
            var provider = provider1.toTypedArray()
            itemView.setOnClickListener {
                var intent = Intent(itemView.context,FlightDetailsPage::class.java)
                intent.putExtra("combolocation",flightModel.originCode+" - "+flightModel.destinationCode)
                intent.putExtra("airport1name",airports!!.get(
                    flightModel.originCode
                )+" International Airport")
                intent.putExtra("airport2name",airports!!.get(
                    flightModel.destinationCode
                )+" International Airport")
                intent.putExtra("flightclass","Non Stop | "+flightModel.`class`+" class")
                intent.putExtra("flightairline",airlines!!.get(
                    flightModel.airlineCode
                ))
                intent.putExtra("departingdate",adays)
                intent.putExtra("arrival_Txt",atime)
                intent.putExtra("landingdate",ddays)
                intent.putExtra("landingdate2",ddays)
                intent.putExtra("depart_txt",dtime)
                intent.putExtra("hour_txt",diff)
                intent.putExtra("farelist",fares)
                Log.i("Providercheck"," :" +provider1.toString())
                intent.putExtra("providerlist",provider)
                itemView.context.startActivity(intent)
            }
        }
        fun getdate(date: Long): Pair<String,String>{
            var arrdate: String = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(java.lang.Long.valueOf(date)),
                ZoneId.systemDefault()
            ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
            var d: Date = sdf.parse(arrdate)
            var cal: Calendar = Calendar.getInstance()
            cal.setTime(d)
//            Log.i("date check",arrdate)
//            Log.i("date check",(cal.get(Calendar.DATE).toString()+" : "+cal.get(Calendar.DAY_OF_WEEK)).toString())
            var day = MainActivity().days[cal.get(Calendar.DAY_OF_WEEK)]
            var date2 = cal.get(Calendar.DATE).toString()
            if(date2.length == 1){
                date2 = "0"+date2
            }
            var month = MainActivity().months[cal.get(Calendar.MONTH)]
            var year = cal.get(Calendar.YEAR)
            var arivalday = day + " "+date2+" "+month+" "+year
            var hours = cal.get(Calendar.HOUR_OF_DAY).toString()
            if(hours.length == 1){
                hours = "0"+hours
            }
            var mins = cal.get(Calendar.MINUTE).toString()
            if(mins.length == 1){
                mins = "0"+mins
            }
            var time = hours+":"+mins
            return Pair(arivalday,time)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.finallistdemo,
            parent,
            false
        )
        return FlightViewHolder(view,airports,airlines,providers)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FlightViewHolder).bind(flightitems!![position],airports,airlines,providers)
    }

    override fun getItemCount(): Int {
        return flightitems!!.size
    }

}