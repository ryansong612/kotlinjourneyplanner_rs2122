package journeyplan

// Add your code for the route planner in this file.

class SubwayMap(private val sMap: List<Segment>) {
  fun routesFrom(origin: Station, destination: Station): List<Route> {
    val visited: MutableList<Station> = mutableListOf()
    val final: MutableList<Route> = mutableListOf()
    val route = mutableListOf<Segment>()

    fun routeMaker(currentStation: Station) {

      if (currentStation != destination) {
        visited.add(currentStation)
      }

      val unvisitedLink = sMap.filter { it.from == currentStation }
        .filter {it.to !in visited }

      if (unvisitedLink.isEmpty()) {
        final.add(Route(route.toMutableList()))
        route.removeLast()
      }

      for (segment in unvisitedLink) {
        route.add(segment)
        if (segment.to != destination) {
          routeMaker(segment.to)
        } else {
          final.add(Route(route.toMutableList()))
          route.clear()
        }
      }
    }

    return if (origin == destination) {
      emptyList()
    } else {
      routeMaker(origin)
      final.filter { it.segments.last().to == destination }
    }
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
  override fun toString(): String {
    var str = ""
    str += "${segments.first().from} to ${segments.last().to}"
    for (seg in segments) {
      str += "\n- ${seg.from} to ${seg.to} by ${seg.line}"
    }
    return str
  }
}

fun londonUnderground(): SubwayMap =
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
    Segment(southKensington, gloucesterRoad, circle, 3)
    )
)

fun main() {
  println(londonUnderground().routesFrom(northActon, southKensington).first())
}
