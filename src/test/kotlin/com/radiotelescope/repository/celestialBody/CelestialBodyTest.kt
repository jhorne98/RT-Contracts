package com.radiotelescope.repository.celestialBody

import com.radiotelescope.TestUtil
import com.radiotelescope.repository.coordinate.Coordinate
import com.radiotelescope.repository.coordinate.ICoordinateRepository
import com.radiotelescope.repository.model.celestialBody.CelestialBodySpecificationBuilder
import com.radiotelescope.repository.model.celestialBody.Filter
import com.radiotelescope.repository.model.celestialBody.SearchCriteria
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
@RunWith(SpringRunner::class)
@ActiveProfiles("test")
internal class CelestialBodyTest {
    @TestConfiguration
    class UtilTestContextConfiguration {
        @Bean
        fun utilService(): TestUtil { return TestUtil() }
    }

    @Autowired
    private lateinit var testUtil: TestUtil

    @Autowired
    private lateinit var celestialBodyRepo: ICelestialBodyRepository

    @Autowired
    private lateinit var coordinateRepo: ICoordinateRepository

    private lateinit var celestialBody: CelestialBody

    @Before
    fun setUp() {
        // CoordinateCreate a coordinate and celestial body
        val coordinate = Coordinate(
                hours = 5,
                minutes = 34,
                seconds = 32,
                rightAscension = Coordinate.hoursMinutesSecondsToDegrees(
                        hours = 5,
                        minutes = 34,
                        seconds = 32
                ),
                declination = 22.0
        )

        coordinateRepo.save(coordinate)

        celestialBody = testUtil.createCelestialBody(
                name = "Crab Nebula",
                coordinate = coordinate
        )

        // CoordinateCreate another one (this will not have a coordinate)
        // and will not be searched for

        testUtil.createCelestialBody(
                name = "The Sun",
                coordinate = null
        )
    }

    @Test
    fun testSearch() {
        val searchCriteria = SearchCriteria(Filter.NAME, "crab Nebula")

        val specification = CelestialBodySpecificationBuilder().with(searchCriteria).build()

        val celestialBodyList = celestialBodyRepo.findAll(specification, PageRequest.of(0, 10))

        assertNotNull(celestialBodyList)
        assertEquals(1, celestialBodyList.content.size)
        assertEquals("Crab Nebula".toLowerCase(), celestialBodyList.content[0].name.toLowerCase())
    }

    @Test
    fun testSearch_Hidden() {
        // Make the celestial body hidden
        celestialBody.status = CelestialBody.Status.HIDDEN
        celestialBodyRepo.save(celestialBody)

        val searchCriteria = SearchCriteria(Filter.NAME, "Crab Nebula")

        val specification = CelestialBodySpecificationBuilder().with(searchCriteria).build()

        val celestialBodyList = celestialBodyRepo.findAll(specification, PageRequest.of(0, 10))

        assertNotNull(celestialBodyList)
        assertEquals(0, celestialBodyList.content.size)
    }
}