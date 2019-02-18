package com.radiotelescope.controller.user

import com.radiotelescope.contracts.user.BaseUserFactory
import com.radiotelescope.contracts.user.UserUserWrapper
import com.radiotelescope.controller.BaseRestControllerTest
import com.radiotelescope.repository.accountActivateToken.IAccountActivateTokenRepository
import com.radiotelescope.repository.role.IUserRoleRepository
import com.radiotelescope.repository.user.IUserRepository
import com.radiotelescope.repository.userNotificationType.IUserNotificationTypeRepository
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired

abstract class BaseUserRestControllerTest : BaseRestControllerTest() {
    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var userRoleRepo: IUserRoleRepository

    @Autowired
    private lateinit var accountActivateTokenRepo: IAccountActivateTokenRepository

    @Autowired
    private lateinit var userNotificationTypeRepo: IUserNotificationTypeRepository

    // These will both be needed in all user rest controller
    // tests, so instantiate them here
    private lateinit var wrapper: UserUserWrapper
    private lateinit var factory: BaseUserFactory

    @Before
    override fun init() {
        super.init()

        factory = BaseUserFactory(
                userRepo = userRepo,
                userRoleRepo = userRoleRepo,
                accountActivateTokenRepo = accountActivateTokenRepo,
                userNotificationTypeRepo = userNotificationTypeRepo
        )

        wrapper = UserUserWrapper(
                context = getContext(),
                factory = factory,
                userRepo = userRepo
        )
    }

    // Once instantiated, this will not be altered
    // so only supply a getter for it
    fun getWrapper(): UserUserWrapper {
        return wrapper
    }
}