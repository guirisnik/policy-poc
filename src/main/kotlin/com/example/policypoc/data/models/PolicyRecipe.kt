package com.example.policypoc.data.models

import com.example.policypoc.controllers.dto.policy.Source
import com.example.policypoc.controllers.dto.policy.Validatable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING
import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.core.convert.converter.Converter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Locale
import java.util.UUID

@Document("PolicyRecipe")
data class PolicyRecipe(
    @Id
    val policyId: PolicyId,
    val recipe: List<Validatable>
) {
    fun sources(): Set<Source> = recipe.flatMap { it.sources }.toSet()
}

data class PolicyId @JsonCreator(mode = DELEGATING) constructor(val value: String) {
    @JsonValue
    override fun toString(): String = value

    companion object {
        private val KEY_REGEX: Regex =
            "[A-Z]{3}-[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}".toRegex()

        fun validFormat(value: String) = KEY_REGEX.matches(value)
        fun withPrefix(prefix: String) =
            PolicyId("$prefix-${UUID.randomUUID()}".uppercase(Locale.getDefault()))
    }
}

internal class PolicyIdToStringConverter : Converter<PolicyId, String> {
    override fun convert(policyId: PolicyId) = policyId.value
}

internal class StringToPolicyIdConverter : Converter<String, PolicyId> {
    override fun convert(str: String) = PolicyId(str)
}