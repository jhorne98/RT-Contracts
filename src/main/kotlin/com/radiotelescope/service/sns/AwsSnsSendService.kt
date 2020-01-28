package com.radiotelescope.service.sns

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.google.common.collect.HashMultimap
import org.springframework.stereotype.Service

@Service
class AwsSnsSendService (
        private val notificationService: AmazonSNS
) : IAwsSnsSendService {
    override fun execute(): HashMultimap<ErrorTag, String>? {
        val publishRequest = PublishRequest("arn:aws:sns:us-east-1:317377631261:testTopic1", "test")
        notificationService.publish(publishRequest)

        // TODO: errors
        return null
    }
}