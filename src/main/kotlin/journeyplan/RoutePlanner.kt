package journeyplan

// Add your code for the route planner in this file.
class SubwayMap(private val sMap: List<Segment>) {
  fun routesFrom(
    origin: Station,
    destination: Station,
    optimisingFor: (Route) -> Int = Route::duration
  ): List<Route> {

    val optimizers = listOf(Route::duration, Route::numChanges)
    if (optimizers.contains(optimisingFor).not()) {
      throw IllegalArgumentException("Invalid Optimising Option")
    }

    val visited: MutableList<Station> = mutableListOf()
    val finalR: MutableList<Route> = mutableListOf()
    val route = mutableListOf<Segment>()

    fun routeMaker(current: Station) {
      // need to check for line, segments with same from but diff line will get added
      if (current != destination) {
        visited.add(current)
      }

      val unvisited = sMap.filter { it.from == current }
        .filter { it.to !in visited }
        .filter { it.line.state == "Normal" }

//      if (unvisited.isEmpty()) {
//        finalR.add(Route(route.toMutableList()))
//        route.removeLast()
//      }

      for (segment in unvisited) {
        route.add(segment)
        if (segment.to != destination) {
          routeMaker(segment.to)
        } else {
          finalR.add(Route(route.toMutableList()))
        }
        route.remove(segment)
      }

      visited.remove(current)
    }

    return if (origin == destination) {
      emptyList()
    } else {
      routeMaker(origin)
      val finalMap = finalR
        .sortedBy { optimisingFor.invoke(it) }
        .filter { r -> canChange(r) }
      // cannot find multi path if second path uses visited station
      finalMap
    }
  }

  fun canChange(route: Route): Boolean {
    val segs = route.segments
    for (i in (0..segs.size - 2)) {
      if (segs[i].line != segs[i + 1].line && segs[i].to.state == "Closed")
        return false
    }
    return true
  }

  override fun toString(): String {
    var str = ""
    for (segment in sMap) {
      str += "$segment\n"
    }
    return str
  }
}

class Route(val segments: List<Segment>) {
  fun duration(): Int = segments.sumOf { x -> x.time }
  fun numChanges(): Int {
    val segs = segments.map { x -> x.line }
    var counter = 0
    for (i in (segs.indices)) {
      if (i == segs.indices.last()) {
        return counter
      }
      if (segs[i] != segs[i + 1]) {
        counter += 1
      }
    }
    return counter
  }

  override fun toString(): String {
    val d = Route(segments).duration()
    val c = Route(segments).numChanges()
    var str =
      "${segments.first().from} to ${segments.last().to} - $d minutes, $c changes"
    val segs = segments.toMutableList()
    println(segs)
    while (segs.size > 0) {
      val first = segs.first()
      segs.remove(first)
      val desL = segs.takeWhile { s -> s.line == first.line }
      desL.forEach { s -> segs.remove(s) }
      str += if (desL.isNotEmpty()) {
        "\n - ${first.from} to ${desL.last().to} by ${first.line}"
      } else {
        "\n - ${first.from} to ${first.to} by ${first.line}"
      }
    }
    return "$str\n"
  }
}

fun londonUnderground1(): SubwayMap =
  SubwayMap(
    listOf(
      Segment(northActon, eastActon, central, 2),
      Segment(eastActon, whiteCity, central, 2),
      Segment(whiteCity, shepherdsBush, central, 4),
      Segment(shepherdsBush, hollandPark, central, 3),
      Segment(hollandPark, nottingHillGate, central, 6),
      Segment(nottingHillGate, highStreetKensington, circle, 4),
      Segment(highStreetKensington, gloucesterRoad, circle, 2),
      Segment(gloucesterRoad, southKensington, circle, 3),

      Segment(eastActon, northActon, central, 2),
      Segment(whiteCity, eastActon, central, 2),
      Segment(shepherdsBush, whiteCity, central, 4),
      Segment(hollandPark, shepherdsBush, central, 3),
      Segment(nottingHillGate, hollandPark, central, 6),
      Segment(highStreetKensington, nottingHillGate, circle, 4),
      Segment(gloucesterRoad, highStreetKensington, circle, 2),
      Segment(southKensington, gloucesterRoad, circle, 3),

      Segment(edgwareRoad, bayswater, district, 5),
      Segment(bayswater, edgwareRoad, district, 5),
      Segment(bayswater, nottingHillGate, district, 4),
      Segment(nottingHillGate, bayswater, district, 4),
      Segment(nottingHillGate, highStreetKensington, district, 4),
      Segment(highStreetKensington, nottingHillGate, district, 4),
      Segment(highStreetKensington, earlsCourt, district, 3),
      Segment(earlsCourt, highStreetKensington, district, 3),
      Segment(earlsCourt, westBrompton, district, 4),
      Segment(westBrompton, earlsCourt, district, 4)
    )
  )

fun main() {
  val piccadillyLine = Line("Piccadilly")
  val victoriaLine = Line("Victoria")
  val districtLine = Line("District")

  val southKensington = Station("South Kensington")
  val knightsbridge = Station("Knightsbridge")
  val hydeParkCorner = Station("Hyde Park Corner")
  val greenPark = Station("Green Park")
  val oxfordCircus = Station("Oxford Circus")
  val victoria = Station("Victoria")
  val sloaneSquare = Station("Sloane Square")

  fun londonUnderground(): SubwayMap = SubwayMap(
    listOf(
      Segment(southKensington, knightsbridge, piccadillyLine, 3),
      Segment(knightsbridge, hydeParkCorner, piccadillyLine, 4),
      Segment(hydeParkCorner, greenPark, piccadillyLine, 2),
      Segment(greenPark, oxfordCircus, victoriaLine, 1),
      Segment(greenPark, victoria, victoriaLine, 1),
      Segment(victoria, greenPark, victoriaLine, 1),
      Segment(victoria, sloaneSquare, districtLine, 6),
      Segment(sloaneSquare, southKensington, districtLine, 3),
      Segment(southKensington, sloaneSquare, districtLine, 6),
      Segment(sloaneSquare, victoria, districtLine, 6),
      Segment(oxfordCircus, greenPark, victoriaLine, 1),
      Segment(knightsbridge, southKensington, piccadillyLine, 3),
      Segment(hydeParkCorner, knightsbridge, piccadillyLine, 4)
    )
  )
  // south ken -> sloane -> victoria via district
  // south ken -> greenpark via pic then greenpark -> vic via victoria //can detect multiroute
  // south ken -> sloane -> victoria, victoria -> greenPark -> Oxf

  val map = londonUnderground()

  var routes = map.routesFrom(southKensington, oxfordCircus)

  println(routes)
}
