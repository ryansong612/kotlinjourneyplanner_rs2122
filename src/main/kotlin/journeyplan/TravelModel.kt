package journeyplan

// Add your code for modelling public transport networks in this file.
class Station (private val name: String) {
  override fun toString() = "$name Station"
}

class Line (private val name: String) {
  override fun toString() = "$name Line"
}

class Segment (private val from: Station,
               private val to: Station,
               private val line: Line,
               private val time: Int) {
  override fun toString() =
    "From: $from, To: $to, via $line using $time minutes"
}
