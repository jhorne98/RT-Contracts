package com.radiotelescope.service.sns

import com.google.common.collect.HashMultimap

interface IAwsSnsSendService {
    fun execute(): HashMultimap<ErrorTag, String>?
}