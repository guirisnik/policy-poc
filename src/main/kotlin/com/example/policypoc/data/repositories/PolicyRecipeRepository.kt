package com.example.policypoc.data.repositories

import com.example.policypoc.data.models.PolicyRecipe
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface PolicyRecipeRepository : MongoRepository<PolicyRecipe, UUID> {
}