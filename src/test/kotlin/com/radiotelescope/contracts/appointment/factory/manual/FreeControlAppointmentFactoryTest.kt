package com.radiotelescope.contracts.appointment.factory.manual

import com.radiotelescope.AbstractSpringTest
import com.radiotelescope.contracts.appointment.manual.AddFreeControlAppointmentCommand
import com.radiotelescope.contracts.appointment.manual.CalibrateFreeControlAppointment
import com.radiotelescope.contracts.appointment.manual.StartFreeControlAppointment
import com.radiotelescope.contracts.appointment.manual.StopFreeControlAppointment
import com.radiotelescope.controller.model.Profile
import com.radiotelescope.repository.allottedTimeCap.IAllottedTimeCapRepository
import com.radiotelescope.repository.appointment.IAppointmentRepository
import com.radiotelescope.repository.coordinate.ICoordinateRepository
import com.radiotelescope.repository.heartbeatMonitor.IHeartbeatMonitorRepository
import com.radiotelescope.repository.orientation.IOrientationRepository
import com.radiotelescope.repository.role.IUserRoleRepository
import com.radiotelescope.repository.telescope.IRadioTelescopeRepository
import com.radiotelescope.repository.user.IUserRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
@RunWith(SpringRunner::class)
internal class FreeControlAppointmentFactoryTest : AbstractSpringTest() {
    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var userRoleRepo: IUserRoleRepository

    @Autowired
    private lateinit var appointmentRepo: IAppointmentRepository

    @Autowired
    private lateinit var radioTelescopeRepo: IRadioTelescopeRepository

    @Autowired
    private lateinit var coordinateRepo: ICoordinateRepository

    @Autowired
    private lateinit var allottedTimeCapRepo: IAllottedTimeCapRepository

    @Autowired
    private lateinit var orientationRepo: IOrientationRepository

    @Autowired
    private lateinit var heartbeatMonitorRepo: IHeartbeatMonitorRepository

    private lateinit var factory: FreeControlAppointmentFactory

    @Before
    fun setUp() {
        factory = FreeControlAppointmentFactory(
                appointmentRepo = appointmentRepo,
                userRepo = userRepo,
                userRoleRepo = userRoleRepo,
                radioTelescopeRepo = radioTelescopeRepo,
                coordinateRepo = coordinateRepo,
                allottedTimeCapRepo = allottedTimeCapRepo,
                orientationRepo = orientationRepo,
                heartbeatMonitorRepo = heartbeatMonitorRepo,
                profile = Profile.TEST
        )
    }

    @Test
    fun start_free_control_appointment() {
        val cmd = factory.startAppointment(
                request = StartFreeControlAppointment.Request(
                        userId = 1L,
                        telescopeId = 1L,
                        duration = 30,
                        hours = 5,
                        minutes = 34,
                        seconds = 32,
                        declination = 22.0,
                        isPublic = true
                )
        )

        // Ensure it is the correct command
        assertTrue(cmd is StartFreeControlAppointment)
    }

    @Test
    fun add_free_control_appointment_command() {
        val cmd = factory.addCommand(
                request = AddFreeControlAppointmentCommand.Request(
                        appointmentId = 1L,
                        hours = 1,
                        minutes = 2,
                        seconds = 3,
                        declination = 4.20
                )
        )

        // Ensure it is the correct command
        assertTrue(cmd is AddFreeControlAppointmentCommand)
    }

    @Test
    fun stop_free_control_appointment() {
        val cmd = factory.stopAppointment(
                appointmentId = 1L
        )

        // Ensure it is the correct command
        assertTrue(cmd is StopFreeControlAppointment)
    }

    @Test
    fun calibrate_free_control_appointment() {
        val cmd = factory.calibrateAppointment(
                appointmentId = 1L
        )

        // Ensure it is the correct command
        assertTrue(cmd is CalibrateFreeControlAppointment)
    }
}