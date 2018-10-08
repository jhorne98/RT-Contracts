package com.radiotelescope.controller.admin.role

import com.google.common.collect.HashMultimap
import com.radiotelescope.contracts.role.ErrorTag
import com.radiotelescope.contracts.role.UserUserRoleWrapper
import com.radiotelescope.controller.BaseRestController
import com.radiotelescope.controller.model.Result
import com.radiotelescope.controller.spring.Logger
import com.radiotelescope.repository.log.Log
import com.radiotelescope.toStringMap
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * REST controller to handle retrieving a [Page] of unapproved user roles
 */
@RestController
class AdminUnapprovedUserRoleListController(
        private val roleWrapper: UserUserRoleWrapper,
        logger: Logger
) : BaseRestController(logger) {
    /**
     * Execute method that is in charge of returning a [Page]
     * of unapproved user roles. If the [PageRequest] does not
     * have valid parameters, it will respond with errors.
     * Otherwise, it will call the [UserUserRoleWrapper.unapprovedList]
     * method, and respond accordingly
     *
     * @param pageNumber the Page Number
     * @param pageSize the Page Size
     */
    @GetMapping(value = ["/users/roles/unapproved"])
    fun execute(@RequestParam("page") pageNumber: Int?,
                @RequestParam("size") pageSize: Int?): Result {
        // If any of the request params are null, respond with errors
        if (pageNumber == null || pageSize == null) {
            val errors = pageErrors()
            // Create error logs
            logger.createErrorLogs(
                    info = errorLog(),
                    errors = errors.toStringMap()
            )
            result = Result(errors = errors.toStringMap())
        }
        // Otherwise call the wrapper method
        else {
            roleWrapper.unapprovedList(PageRequest.of(pageNumber, pageSize)) { it ->
                // If the command was a success
                it.success?.let { page ->
                    page.content.forEach {
                        logger.createSuccessLog(successLog(it.id))
                    }
                    result = Result(data = it)
                }
                // If the command was a failure
                it.error?.let { errors ->
                    logger.createErrorLogs(
                            info = errorLog(),
                            errors = errors.toStringMap()
                    )
                    result = Result(errors = errors.toStringMap())
                }
            }?.let {
                // If we get here, this means the User did not pass authentication
                // Create error logs
                logger.createErrorLogs(errorLog(), it.toStringMap())
                result = Result(
                        errors = it.toStringMap(),
                        status = HttpStatus.FORBIDDEN
                )
            }
        }

        return result
    }

    /**
     * Private method that will return erros if any of the parameters
     * are not valid
     */
    private fun pageErrors(): HashMultimap<ErrorTag, String> {
        val errors = HashMultimap.create<ErrorTag, String>()
        errors.put(ErrorTag.PAGE_PARAMS, "Invalid Page parameters")
        return errors
    }

    /**
     * Override of the [BaseRestController.errorLog] method that
     * returns a controller-specific [Logger.Info]
     */
    override fun errorLog(): Logger.Info {
        return Logger.Info(
                affectedTable = Log.AffectedTable.USER_ROLE,
                action = Log.Action.RETRIEVE,
                timestamp = Date(),
                affectedRecordId = null
        )
    }

    /**
     * Override of the [BaseRestController.successLog] method that
     * returns a controller specific [Logger.Info]
     */
    override fun successLog(id: Long): Logger.Info {
        return Logger.Info(
                affectedTable = Log.AffectedTable.USER_ROLE,
                action = Log.Action.RETRIEVE,
                timestamp = Date(),
                affectedRecordId = id
        )
    }
}