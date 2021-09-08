package com.example.policypoc.services.validation

import com.example.policypoc.controllers.dto.policy.Method
import org.apache.commons.text.similarity.LevenshteinDistance

fun validate(method: Method, params: Map<String, Any>) : Any =
    when (method) {
        Method.SIMILARITY -> ::similarity.parameters.associateWith { params[it.name] }.let(::similarity::callBy)
        Method.REGISTRATION_STATUS -> ::registrationStatus.parameters.associateWith { params[it.name] }.let(::registrationStatus::callBy)
        Method.ONE_OF -> ::oneOf.parameters.associateWith { params[it.name] }.let(::oneOf::callBy)
    }

fun similarity(source: String, target: String, factor: Double) : Boolean {
    val maximumLength = maxOf(source.length, target.length)
    val similarity : Double = ((maximumLength - LevenshteinDistance().apply(source, target)) / maximumLength).toDouble()
    return similarity >= factor
}

fun registrationStatus(input: String) {
    TODO("Check registration status of input")
}

fun oneOf(input: String, acceptedValues: List<String>) {
    TODO("Check if input is one of the accepted values")
}