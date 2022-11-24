package journeyplan

// Add your code for modelling public transport networks in this file.
class Station(val name: String, var state: String = "Open") {
  fun close() {
    state = "Closed"
  }

  fun open() {
    state = "Open"
  }

  override fun toString(): String = name
}

class Line(val name: String, var state: String = "Normal") {
  fun suspend() {
    state = "Suspended"
  }

  fun resume() {
    state = "Normal"
  }

  override fun toString(): String = "$name Line"
}

class Segment(
  val from: Station,
  val to: Station,
  val line: Line,
  val time: Int
) {
  override fun toString() =
    "From: $from, To: $to, via $line using $time minutes"
}

val northActon = Station("North Acton")
val eastActon = Station("East Acton")
val whiteCity = Station("White City")
val shepherdsBush = Station("Shepherd's Bush")
val hollandPark = Station("Holland Park")
val nottingHillGate = Station("Notting Hill Gate")
val highStreetKensington = Station("High Street Kensington")
val gloucesterRoad = Station("Gloucester Road")
val southKensington = Station("South Kensington")
val edgwareRoad = Station("Edgware Road")
val bayswater = Station("Bayswater")
val earlsCourt = Station("Earl's Court")
val westBrompton = Station("West Brompton")
val central = Line("Central")
val circle = Line("Circle")
val district = Line("Distict")
