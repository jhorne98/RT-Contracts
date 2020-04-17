package com.radiotelescope.controller.user

import com.radiotelescope.contracts.user.UserUserWrapper
import com.radiotelescope.contracts.user.Update
import com.radiotelescope.controller.BaseRestController
import com.radiotelescope.controller.model.user.UpdateForm
import com.radiotelescope.controller.model.Result
import com.radiotelescope.toStringMap
import com.radiotelescope.controller.spring.Logger
import com.radiotelescope.security.AccessReport
import com.radiotelescope.repository.log.Log
import com.radiotelescope.repository.user.IUserRepository
import com.radiotelescope.service.s3.IAwsS3DeleteService
import com.radiotelescope.service.s3.IAwsS3UploadService
import liquibase.util.file.FilenameUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

/**
 * REST Controller to handle update User information
 *
 * @param userWrapper the [UserUserWrapper]
 * @param logger the [Logger] service
 */
@RestController
class UserUpdateController(
        private val userWrapper: UserUserWrapper,
        private val uploadService: IAwsS3UploadService,
        private val deleteService: IAwsS3DeleteService,
        private val userRepo: IUserRepository,
        logger: Logger
) : BaseRestController(logger){
    /**
     * Execute method that is in charge of taking the [UpdateForm]
     * and adapting it to the a [Update.Request] if possible.
     * If it is not able to, it will respond with errors.
     *
     * Otherwise, it will execute the [UserUserWrapper.update] method. If
     * this method returns an [AccessReport] respond with errors. If not,
     * this means the [Update] command was executed, check if the method
     * was a success or not
     *
     * @param userId the User's id
     * @param form the [UpdateForm] object
     */
    @CrossOrigin(value = ["http://localhost:8081"])
    @PutMapping(value = ["/api/users/{userId}"], consumes = ["multipart/form-data"])
    fun execute(@PathVariable("userId") userId: Long,
                @RequestPart("file") @Valid file: MultipartFile,
                @RequestBody form: UpdateForm): Result {

        // check if a file is being uploaded
        if (!file.isEmpty) {
            // if a profile picture exists from the user, delete it
            val theUser = userRepo.findById(userId).get()
            if (theUser.profilePicture != null) {
                deleteService(theUser.profilePicture)
            }

            // generate a file path to replace the old one
            val generatedPath = theUser.firstName + theUser.lastName + System.currentTimeMillis() + FilenameUtils.getExtension(file.originalFilename)
            uploadService.execute(file, generatedPath)
        }

        // If the form validation fails, respond with errors
        form.validateRequest()?.let {
            // Create error logs
            logger.createErrorLogs(
                    info = Logger.createInfo(
                            affectedTable = Log.AffectedTable.USER,
                            action = "User Update",
                            affectedRecordId = null,
                            status = HttpStatus.BAD_REQUEST.value()
                    ),
                    errors = it.toStringMap()
            )

            result = Result(errors = it.toStringMap())
        } ?: let {
            // Otherwise call the factory command
            userWrapper.update(
                    request = form.toRequest()
            ) { response ->
                // If the command was a success
                response.success?.let { data ->
                    result = Result(
                            data = data
                    )

                    logger.createSuccessLog(
                            info = Logger.createInfo(
                                    affectedTable = Log.AffectedTable.USER,
                                    action = "User Update",
                                    affectedRecordId = data,
                                    status = HttpStatus.OK.value()
                            )
                    )
                }
                // Otherwise, it was a failure
                response.error?.let { error ->
                    // Create error logs
                    logger.createErrorLogs(
                            info = Logger.createInfo(
                                    affectedTable = Log.AffectedTable.USER,
                                    action = "User Update",
                                    affectedRecordId = null,
                                    status = HttpStatus.BAD_REQUEST.value()
                            ),
                            errors = error.toStringMap()
                    )
                    result = Result(
                            errors = error.toStringMap()
                    )
                }
            }?.let { report ->
                // If we get here, that means the user was not authenticated
                // Create error logs
                logger.createErrorLogs(
                        info = Logger.createInfo(
                                affectedTable = Log.AffectedTable.USER,
                                action = "User Update",
                                affectedRecordId = null,
                                status = if (report.missingRoles != null) HttpStatus.FORBIDDEN.value() else HttpStatus.NOT_FOUND.value()
                        ),
                        errors = if (report.missingRoles != null) report.toStringMap() else report.invalidResourceId!!
                )

                // Set the errors depending on if the user was not authenticated or the
                // record did not exists
                result = if (report.missingRoles == null) {
                    Result(errors = report.invalidResourceId!!, status = HttpStatus.NOT_FOUND)
                }
                // user did not have access to the resource
                else {
                    Result(errors = report.toStringMap(), status = HttpStatus.FORBIDDEN)
                }
            }
        }

        return result
    }
}