package com.example.flightsearchapp

data class FlightResponse(
    val appendix: Appendix,
    val flights: List<Flight>
)

data class Appendix(
    val airlines: HashMap<String,String>,
    val airports: HashMap<String,String>,
    val providers: HashMap<Integer,String>
)

data class Flight(
    val airlineCode: String,
    val arrivalTime: Long,
    val `class`: String,
    val departureTime: Long,
    val destinationCode: String,
    val fares: List<Fare>,
    val originCode: String
)

data class Fare(
    val fare: Int,
    val providerId: Int
)