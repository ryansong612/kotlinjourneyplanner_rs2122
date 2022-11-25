package journeyplan

import org.junit.Assert.assertEquals
import org.junit.Test

class TravelModelTest {
  @Test
  fun `printing stations shows their names`() {
    assertEquals("South Kensington", Station("South Kensington").toString())
    assertEquals("Knightsbridge", Station("Knightsbridge").toString())
    assertEquals("North Acton", Station("North Acton").toString())
    assertEquals("East Acton", eastActon.toString())
  }

  @Test
  fun `printing lines shows their names`() {
    assertEquals("District Line", Line("District").toString())
    assertEquals("Circle Line", Line("Circle").toString())
  }
}
