package com.example.policypoc.infrastructure.exceptions

class EntityNotFoundException(
    private val m: String,
    private val c: Throwable? = null
) : Exception(m, c)