package com.radiotelescope.controller.model.appointment

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.radiotelescope.contracts.appointment.ErrorTag
import com.radiotelescope.contracts.appointment.UserFutureList
import com.radiotelescope.controller.model.BaseForm

class ListFutureAppointmentByUserForm(
    val userID: Long
): BaseForm<UserFutureList.Request> {

    override fun toRequest(): UserFutureList.Request {
        return UserFutureList.Request(
            userId = userID!!
        )
    }

//    fun validateRequest(): Multimap<ErrorTag, String>? {
//        val errors = HashMultimap.create<ErrorTag, String>()
//
//        if (userID == null)
//            error.put(ErrorTag.USER_ID, "invalid")
//    }
}