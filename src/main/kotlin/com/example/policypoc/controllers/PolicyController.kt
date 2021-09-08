package com.example.policypoc.controllers

import com.example.policypoc.controllers.dto.policy.RecipeRequest
import com.example.policypoc.controllers.dto.policy.RecipeResponse
import com.example.policypoc.data.models.PolicyRecipe
import com.example.policypoc.infrastructure.exceptions.EntityNotFoundException
import com.example.policypoc.infrastructure.extensions.fold
import com.example.policypoc.services.recipe.RecipeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import java.util.UUID

@RestController
class PolicyController(
    private val recipeService: RecipeService
) {

    @PostMapping("/policy")
    fun create(@RequestBody recipe: RecipeRequest): ResponseEntity<RecipeResponse> =
        recipeService
            .save(recipe)
            .let { ResponseEntity.status(HttpStatus.CREATED).body(RecipeResponse(it.id)) }

    @GetMapping("/policy/{id}")
    fun read(@PathVariable id: UUID): ResponseEntity<PolicyRecipe> = recipeService
        .find(id)
        .fold(
            { ResponseEntity.status(HttpStatus.OK).body(it) },
            { throw EntityNotFoundException("Policy with id $id was not found") }
        )
}