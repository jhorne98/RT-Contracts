package com.radiotelescope.controller.user

import com.radiotelescope.contracts.user.UserInfo
import com.radiotelescope.controller.model.user.LoginForm
import com.radiotelescope.repository.log.ILogRepository
import com.radiotelescope.repository.user.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
@RunWith(SpringRunner::class)
internal class UserLoginControllerTest : BaseUserRestControllerTest() {
    @Autowired
    private lateinit var logRepo: ILogRepository

    private lateinit var userLoginController: UserLoginController

    private lateinit var user: User

    private lateinit var baseForm: LoginForm

    @Before
    override fun init() {
        super.init()

        user = testUtil.createUserWithEncodedPassword(
                email = "rpim@ycp.edu",
                password = "Password"
        )
        testUtil.createAllottedTimeCapForUser(
                user = user,
                allottedTime = 0L
        )

        userLoginController = UserLoginController(
                userWrapper = getWrapper(),
                logger = getLogger()
        )

        baseForm = LoginForm(
                email = user.email,
                password = "Password"
        )
    }

    @Test
    fun testSuccessResponse() {
        // Test the success scenario to ensure the result
        // object is correctly set
        val result = userLoginController.execute(baseForm)

        assertNotNull(result)
        assertTrue(result.data is UserInfo)
        assertEquals(HttpStatus.OK, result.status)
        assertNull(result.errors)

        // Ensure a log was created
        assertEquals(1, logRepo.count())

        logRepo.findAll().forEach {
            assertEquals(HttpStatus.OK.value(), it.status)
        }
    }

    @Test
    fun testInvalidFormResponse() {
        // Test the scenario where the form's validate
        // request method fails to ensure the result object
        // has the correct properties
        val formCopy = baseForm.copy(email = "")

        val result = userLoginController.execute(formCopy)

        assertNotNull(result)
        assertNull(result.data)
        assertNotNull(result.errors)
        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals(1, result.errors!!.size)

        // Ensure a log was created
        assertEquals(1, logRepo.count())

        logRepo.findAll().forEach {
            assertEquals(HttpStatus.BAD_REQUEST.value(), it.status)
        }
    }

    @Test
    fun testFailedValidationResponse() {
        // Test the scenario where the validation
        // in the command object fails
        val formCopy = baseForm.copy(email = "r@ycp.edu")

        val result = userLoginController.execute(formCopy)

        assertNotNull(result)
        assertNull(result.data)
        assertNotNull(result.errors)
        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals(1, result.errors!!.size)

        // Ensure a log was created
        assertEquals(1, logRepo.count())

        logRepo.findAll().forEach {
            assertEquals(HttpStatus.BAD_REQUEST.value(), it.status)
        }
    }
}