package com.example.policypoc.data.models

import com.example.policypoc.controllers.dto.policy.Source
import com.example.policypoc.controllers.dto.policy.Validation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document
data class PolicyRecipe(
    @Id
    val id: UUID,
    val recipe: List<Validation>
) {
    fun sources(): Set<Source> =
        recipe.flatMap { it.sources }.toSet()
}
