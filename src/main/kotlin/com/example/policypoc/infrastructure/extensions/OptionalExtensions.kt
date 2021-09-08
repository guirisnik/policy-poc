package com.example.policypoc.infrastructure.extensions

import com.example.policypoc.infrastructure.exceptions.EntityNotFoundException
import java.util.Optional

fun <T : Any, M : Any> Optional<M>.fold(
    presentStrategy: ((M) -> T),
    emptyStrategy: (() -> T) = { throw EntityNotFoundException("Entity not found.") }
): T =
    when {
        isPresent -> presentStrategy(get())
        else -> emptyStrategy()
    }