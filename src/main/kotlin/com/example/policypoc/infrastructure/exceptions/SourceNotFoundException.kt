package com.example.policypoc.infrastructure.exceptions

class SourceNotFoundException(
    private val m: String,
    private val c: Throwable? = null
) : Exception(m, c)