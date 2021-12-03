package com.example.policypoc.data.repositories

import com.example.policypoc.data.models.PolicyId
import com.example.policypoc.data.models.PolicyRecipe
import org.springframework.data.mongodb.repository.MongoRepository

interface PolicyRecipeRepository : MongoRepository<PolicyRecipe, PolicyId>