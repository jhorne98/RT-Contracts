package com.radiotelescope.repository.model.user

import com.radiotelescope.repository.user.User

/**
 * Enum class that acts as a search filter for the [User] entity
 *
 * @param field the corresponding entity field. Note: NOT the corresponding SQL column
 */
enum class Filter(val field: String) {
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    COMPANY("company")
}