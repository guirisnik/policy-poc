package com.example.policypoc.services.validation

import com.example.policypoc.controllers.dto.policy.Method
import org.apache.commons.text.similarity.LevenshteinDistance

enum class AnalysisStatus {
    APPROVED,
    GREY_ZONE,
    REJECTED
}

object ParameterValidation {
    fun validate(method: Method, params: Map<String, Any>): Any =
        when (method) {
            Method.SIMILARITY -> ::similarity.parameters.associateWith { params[it.name] }.let(::similarity::callBy)
            Method.REGISTRATION_STATUS -> ::registrationStatus.parameters.associateWith { params[it.name] }
                .let(::registrationStatus::callBy)
            Method.ONE_OF -> ::oneOf.parameters.associateWith { params[it.name] }.let(::oneOf::callBy)
        }

    fun similarity(source: String, target: String, factor: Double): Boolean =
        maxOf(source.length, target.length)
            .let { ((it - LevenshteinDistance().apply(source, target)) / it).toDouble() }
            .let { it >= factor }


    fun registrationStatus(input: String) {
        TODO("Check registration status of input")
    }

    fun oneOf(input: String, acceptedValues: List<String>) {
        TODO("Check if input is one of the accepted values")
    }
}