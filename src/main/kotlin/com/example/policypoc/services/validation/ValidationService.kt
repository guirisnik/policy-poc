package com.example.policypoc.services.validation

import com.example.policypoc.adapters.PersonalDataAdapter
import com.example.policypoc.controllers.dto.validation.ValidationRequest
import com.example.policypoc.data.repositories.PolicyRecipeRepository
import com.example.policypoc.infrastructure.exceptions.EntityNotFoundException
import com.example.policypoc.infrastructure.extensions.fold
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ValidationService(
    private val personalDataAdapter: PersonalDataAdapter,
    private val policyRecipeRepository: PolicyRecipeRepository
) {
    fun validate(policyId: UUID, request: ValidationRequest) =
        policyRecipeRepository
            .findById(policyId)
            .fold(
                {
                    it.sources()
                        .associateWith { source -> personalDataAdapter.execute(source, request.params) }
                    // TODO: Apply validations against vendor results
                },
                { throw EntityNotFoundException("Policy with id $policyId was not found") }
            )
}