package com.radiotelescope.contracts.appointment.info

import com.radiotelescope.contracts.coordinate.CoordinateInfo
import com.radiotelescope.repository.appointment.Appointment
import java.util.*
import kotlin.collections.ArrayList

/**
 * Subclass of [AppointmentInfo] for the "Raster Scan" Appointment type
 *
 * @param coordinates a list of [CoordinateInfo] objects
 */
data class RasterScanAppointmentInfo(
        override val id: Long,
        override val startTime: Date,
        override val endTime: Date,
        override val telescopeId: Long,
        override val isPublic: Boolean,
        override val userId: Long,
        override val userFirstName: String,
        override val userLastName: String,
        override val userEmail: String,
        override val status: String,
        override val type: String,
        val coordinates: ArrayList<CoordinateInfo> = arrayListOf()
) : AppointmentInfo(
        id = id,
        startTime = startTime,
        endTime = endTime,
        telescopeId = telescopeId,
        isPublic = isPublic,
        userId = userId,
        userFirstName = userFirstName,
        userLastName = userLastName,
        userEmail = userEmail,
        status = status,
        type = type
) {
    /**
     * Secondary constructor that takes an appointment object
     * to set all fields
     *
     * @param appointment the Appointment
     */
    constructor(appointment: Appointment) : this(
            id = appointment.id,
            startTime = appointment.startTime,
            endTime = appointment.endTime,
            telescopeId = appointment.telescopeId,
            isPublic = appointment.isPublic,
            userId = appointment.user.id,
            userFirstName = appointment.user.firstName,
            userLastName = appointment.user.lastName,
            userEmail = appointment.user.email,
            status = appointment.status.label,
            type = appointment.type.label
    ) {
        appointment.coordinateList.forEach {
            coordinates.add(CoordinateInfo(it))
        }
    }
}