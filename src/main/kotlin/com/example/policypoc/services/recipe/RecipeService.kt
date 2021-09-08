package com.example.policypoc.services.recipe

import com.example.policypoc.controllers.dto.policy.RecipeRequest
import com.example.policypoc.data.models.PolicyRecipe
import com.example.policypoc.data.repositories.PolicyRecipeRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RecipeService(
    private val policyRecipeRepository: PolicyRecipeRepository
) {
    fun save(recipeRequest: RecipeRequest) =
        policyRecipeRepository.save(
            PolicyRecipe(
                id = UUID.randomUUID(),
                recipe = recipeRequest.policy
            )
        )

    fun find(id: UUID) =
        policyRecipeRepository.findById(id)
}