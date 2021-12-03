package com.example.policypoc.services.recipe

import com.example.policypoc.controllers.dto.policy.RecipeRequest
import com.example.policypoc.data.models.PolicyId
import com.example.policypoc.data.models.PolicyRecipe
import com.example.policypoc.data.repositories.PolicyRecipeRepository
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val policyRecipeRepository: PolicyRecipeRepository
) {
    fun save(recipeRequest: RecipeRequest) =
        policyRecipeRepository.save(
            PolicyRecipe(
                policyId = PolicyId.withPrefix("POL"),
                recipe = recipeRequest.policy
            )
        )

    fun find(id: PolicyId): PolicyRecipe =
        policyRecipeRepository
            .findById(id)
            .orElseThrow()
}