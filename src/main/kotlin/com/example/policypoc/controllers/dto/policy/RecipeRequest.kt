package com.example.policypoc.controllers.dto.policy

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.resolve.constants.KClassValue

data class RecipeRequest(
    val policy: List<Validatable>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Validation(
    val parameter: Parameter,
    val sources: Set<Source>,
    val method: Method,
    val configuration: Map<ConfigurationParameter, Any>? = null
)

enum class Parameter {
    DOCUMENT,
    NAME,
    ADDRESS,
    EMAIL,
    PHONE
}

enum class Source {
    PH3A,
    NEOWAY
}

enum class Method(requiredConfiguration: Set<ConfigurationParameter>?) {
    SIMILARITY(setOf(ConfigurationParameter.FACTOR)),
    REGISTRATION_STATUS(null),
    ONE_OF(setOf(ConfigurationParameter.ACCEPTED_VALUES))
}

enum class ConfigurationParameter {
    @JsonProperty("factor")
    FACTOR,

    @JsonProperty("acceptedValues")
    ACCEPTED_VALUES
}
