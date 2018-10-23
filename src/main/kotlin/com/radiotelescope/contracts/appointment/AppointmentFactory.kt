package com.radiotelescope.contracts.appointment

import com.google.common.collect.Multimap
import com.radiotelescope.contracts.Command
import com.radiotelescope.repository.appointment.Appointment
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

/**
 * Abstract factory interface with methods for all [Appointment] CRUD operations
 */
interface AppointmentFactory {
    /**
     * Abstract command used to schedule an appointment
     *
     * @param request the [Create.Request] request
     * @return a [Command] object
     */
    fun create(request: Create.Request): Command<Long, Multimap<ErrorTag, String>>

    /**
     * Abstract command used to cancel an appointment
     *
     * @param appointmentId the Appointment id
     * @return a [Command] object
     */
    fun cancel(appointmentId: Long): Command<Long, Multimap<ErrorTag,String>>

    /**
     * Abstract command used to retrieve appointment information
     *
     * @param id the Appointment's id
     * @return a [Command] object
     */
    fun retrieve(id: Long): Command<AppointmentInfo, Multimap<ErrorTag,String>>

    /**
     * Abstract command user to retrieve completed appointments for a user
     *
     * @param userId the User id
     * @return a [Command] object
     */
    fun pastAppointmentListForUser(userId: Long, pageRequest: PageRequest): Command <Page<AppointmentInfo>, Multimap<ErrorTag,String>>

    /**
     * Abstract command used to update an appointment
     * @param request the [Update.Request]
     * @return [Update] [Command] object
     */
    fun update(request: Update.Request): Command<Long, Multimap<ErrorTag, String>>

    /**
     * Abstract command user to retrieve all appointments for a telescope
     *
     * @param telescopeId the Telescope id
     * @return a [Command] object
     */
    fun retrieveByTelescopeId(telescopeId: Long, pageRequest: PageRequest): Command<Page<AppointmentInfo>, Multimap<ErrorTag, String>>

    fun retrieveFutureAppointmentsByTelescopeId(telescopeId: Long, pageRequest: PageRequest): Command<Page<AppointmentInfo>, Multimap<ErrorTag, String>>

    /**
     * Abstract command used to retrieve a user's future appointments
     *
     * @param userId the User's id
     * @param pageable the [Pageable] interface
     * @return a [Command] object
     */
    fun getFutureAppointmentsForUser(userId: Long, pageable: Pageable): Command<Page<AppointmentInfo>, Multimap<ErrorTag,String>>

}