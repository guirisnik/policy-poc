package com.example.policypoc.infrastructure.exceptions

class MissingMandatoryParameterException(
    private val m: String,
    private val c: Throwable? = null
) : Exception(m, c)