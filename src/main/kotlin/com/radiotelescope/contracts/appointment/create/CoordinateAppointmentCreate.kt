package com.radiotelescope.contracts.appointment.create

import com.radiotelescope.contracts.BaseCreateRequest
import com.radiotelescope.contracts.Command
import com.radiotelescope.repository.appointment.IAppointmentRepository
import com.radiotelescope.repository.appointment.Appointment
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.radiotelescope.contracts.SimpleResult
import com.radiotelescope.contracts.appointment.ErrorTag
import com.radiotelescope.repository.coordinate.ICoordinateRepository
import com.radiotelescope.repository.coordinate.Coordinate
import com.radiotelescope.repository.role.IUserRoleRepository
import com.radiotelescope.repository.telescope.ITelescopeRepository
import com.radiotelescope.repository.user.IUserRepository
import java.util.*

/**
 * Override of the [Command] interface method used for Point Appointment creation
 *
 * @param request the [Request] object
 * @param appointmentRepo the [IAppointmentRepository] interface
 * @param userRepo the [IUserRepository] interface
 * @param userRoleRepo the [IUserRoleRepository] interface
 * @param telescopeRepo the [ITelescopeRepository] interface
 * @param coordinateRepo the [ICoordinateRepository] interface
 */
class CoordinateAppointmentCreate(
        private val request: Request,
        private val appointmentRepo: IAppointmentRepository,
        private val userRepo: IUserRepository,
        private val userRoleRepo: IUserRoleRepository,
        private val telescopeRepo: ITelescopeRepository,
        private val coordinateRepo: ICoordinateRepository
) : Command<Long, Multimap<ErrorTag, String>>, Create {
    /**
     * Override of the [Command.execute] method. Calls the [validateRequest] method
     * that will handle all constraint checking and validation.
     *
     * If validation passes, it will create and persist the [Appointment] object and
     * return the id in the [SimpleResult] object.
     *
     * If validation fails, it will return a [SimpleResult] with the errors
     */
    override fun execute(): SimpleResult<Long, Multimap<ErrorTag, String>> {
        validateRequest()?.let { return SimpleResult(null, it) } ?: let {
            val theAppointment = request.toEntity()

            val theCoordinate = request.toCoordinate()
            coordinateRepo.save(theCoordinate)

            theAppointment.user = userRepo.findById(request.userId).get()

            // "Point" Appointments will have a single Coordinate
            theAppointment.coordinateList = arrayListOf(theCoordinate)
            appointmentRepo.save(theAppointment)

            theCoordinate.appointment = theAppointment
            coordinateRepo.save(theCoordinate)

            return SimpleResult(theAppointment.id, null)
        }
    }

    /**
     * Method responsible for constraint checking and validations for the
     * appointment create request. It will ensure that both the user and telescope
     * id exists and that the appointment's end time is not before its start time.
     * It also ensures that the start time is not before the current date.
     *
     * Specific to the appointment type, it will make sure the right ascension and
     * declination supplied are valid.
     */
    private fun validateRequest(): Multimap<ErrorTag, String>? {
        basicValidateRequest(
                request = request,
                userRepo = userRepo,
                telescopeRepo = telescopeRepo,
                appointmentRepo = appointmentRepo
        )?.let { return it }

        var errors = HashMultimap.create<ErrorTag, String>()

        with(request) {
            if (hours < 0 || hours >= 24)
                errors!!.put(ErrorTag.HOURS, "Hours must be between 0 and 24")
            if (minutes < 0 || minutes >= 60)
                errors!!.put(ErrorTag.MINUTES, "Minutes must be between 0 and 60")
            if (seconds < 0 || seconds >= 60)
                errors!!.put(ErrorTag.SECONDS, "Seconds must be between 0 and 60")
            if (declination > 90 || declination < -90)
                errors!!.put(ErrorTag.DECLINATION, "Declination must be between -90 and 90")

            if (!errors!!.isEmpty)
                return errors

            errors = validateAvailableAllottedTime(
                    request = request,
                    appointmentRepo = appointmentRepo,
                    userRoleRepo = userRoleRepo
            )

        }


      return if (errors!!.isEmpty) null else errors
    }

    /**
     * Data class containing all fields necessary for appointment creation. Implements
     * the [BaseCreateRequest] interface.
     */
    data class Request(
            override val userId: Long,
            override val startTime: Date,
            override val endTime: Date,
            override val telescopeId: Long,
            override val isPublic: Boolean,
            val hours: Int,
            val minutes: Int,
            val seconds: Int,
            val declination: Double
    ) : Create.Request() {
        /**
         * Concrete implementation of the [BaseCreateRequest.toEntity] method that
         * returns an Appointment object
         */
        override fun toEntity(): Appointment {
            return Appointment(
                    startTime = startTime,
                    endTime = endTime,
                    telescopeId = telescopeId,
                    isPublic = isPublic,
                    type = Appointment.Type.POINT
            )
        }

        /**
         * Method that will take the [Request] hours, minutes, seconds, and declination
         * and returns a [Coordinate] object
         */
        fun toCoordinate(): Coordinate {
            return Coordinate(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                    rightAscension = Coordinate.hoursMinutesSecondsToDegrees(
                            hours = hours,
                            minutes = minutes,
                            seconds = seconds
                    ),
                    declination = declination
            )
        }
    }
}