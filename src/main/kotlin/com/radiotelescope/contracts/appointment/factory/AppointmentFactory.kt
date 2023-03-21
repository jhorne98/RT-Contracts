package com.radiotelescope.contracts.appointment.factory

import com.google.common.collect.Multimap
import com.radiotelescope.contracts.Command
import com.radiotelescope.contracts.appointment.*
import com.radiotelescope.contracts.appointment.info.AppointmentInfo
import com.radiotelescope.repository.appointment.Appointment
import com.radiotelescope.repository.model.appointment.SearchCriteria
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Abstract factory interface with methods for all [Appointment] Command objects
 */
interface AppointmentFactory {

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
    fun userCompletedList(userId: Long, pageable: Pageable): Command <Page<AppointmentInfo>, Multimap<ErrorTag,String>>

    /**
     * Abstract command used to retrieve all future appointments for a telescope
     *
     * @param telescopeId the Telescope id
     * @param pageable the [Pageable] interface
     * @return a [Command] object
     */
    fun retrieveFutureAppointmentsByTelescopeId(telescopeId: Long, pageable: Pageable): Command<Page<AppointmentInfo>, Multimap<ErrorTag, String>>

    /**
     * Abstract command used to retrieve a user's future appointments
     *
     * @param userId the User's id
     * @param pageable the [Pageable] interface
     * @return a [Command] object
     */
    fun userFutureList(userId: Long, request: UserFutureList.Request): Command<List<Appointment>, Multimap<ErrorTag,String>>

    /**
     * Abstract command used to retrieve a list of appointments between two time
     *
     * @param request the [ListBetweenDates.Request] object
     * @return a [Command] object
     */
    fun listBetweenDates(request: ListBetweenDates.Request): Command<List<AppointmentInfo>, Multimap<ErrorTag,String>>

    /**
     * Abstract command used to make an appointment public
     *
     * @param appointmentId the Appointment's id
     * @return a [Command] object
     */
    fun makePublic(appointmentId: Long): Command<Long, Multimap<ErrorTag, String>>

    /**
     * Abstract command used to retrieve public, completed appointments
     *
     * @param pageable the Pageable interface
     * @return a [Command] object
     */
    fun publicCompletedAppointments(pageable: Pageable): Command<Page<AppointmentInfo>, Multimap<ErrorTag, String>>

    /**
     * Abstract command user to retrieve completed appointments for a user
     *
     * @param pageable the [Pageable] object, that has the page number and page size
     * @return a [Command] object
     */
    fun requestedList(pageable: Pageable): Command<Page<AppointmentInfo>, Multimap<ErrorTag, String>>

    /**
     * Abstract command used to approve or deny an appointment request
     *
     * @param request the [ApproveDenyRequest.Request] object
     * @return a [Command] object
     */
    fun approveDenyRequest(request: ApproveDenyRequest.Request): Command<Long, Multimap<ErrorTag, String>>

    /**
     * Abstract command used to retrieve the available time for a user
     *
     * @param userId the User's Id
     * @return a [Command] object
     */
    fun userAvailableTime(userId: Long): Command<Long, Multimap<ErrorTag, String>>

    /**
     * Abstract command used to search for appointments
     *
     * @param searchCriteria a [List] of [SearchCriteria]
     * @param pageable the [Pageable] object
     * @return a [Command] object
     */
    fun search(searchCriteria: List<SearchCriteria>, pageable: Pageable): Command<Page<AppointmentInfo>, Multimap<ErrorTag, String>>
}