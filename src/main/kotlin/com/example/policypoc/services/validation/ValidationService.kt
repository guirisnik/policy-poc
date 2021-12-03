package com.example.policypoc.services.validation

import com.example.policypoc.adapters.PersonalDataAdapter
import com.example.policypoc.controllers.dto.policy.DocumentValidation
import com.example.policypoc.controllers.dto.policy.NameValidation
import com.example.policypoc.controllers.dto.policy.Parameter
import com.example.policypoc.controllers.dto.policy.PhoneValidation
import com.example.policypoc.controllers.dto.policy.Source
import com.example.policypoc.controllers.dto.validation.ValidationRequest
import com.example.policypoc.data.models.PolicyId
import com.example.policypoc.data.models.PolicyRecipe
import com.example.policypoc.data.repositories.PolicyRecipeRepository
import org.springframework.stereotype.Service

@Service
class ValidationService(
    private val personalDataAdapter: PersonalDataAdapter,
    private val policyRecipeRepository: PolicyRecipeRepository
) {
    fun validate(policyId: PolicyId, request: ValidationRequest): PolicyResult {
        val policy: PolicyRecipe = policyRecipeRepository.findById(policyId).orElseThrow()

        val personalDataSource =
            policy.sources().associateWith { personalDataAdapter.execute(it, request.personalData) }

        val fields: List<Field> = policy.recipe
            .mapNotNull {
                when (it) {
                    is NameValidation -> it.toField(personalDataSource, request.personalData)
                    is DocumentValidation -> it.toField(personalDataSource)
                    is PhoneValidation -> it.toField(personalDataSource, request.personalData)
                    else -> null
                }
            }

        return PolicyResult(
            policyId,
            fields.converge(),
            fields
        )
    }

    private fun List<Field>.converge(): AnalysisStatus =
        when {
            any { it.status == AnalysisStatus.REJECTED } -> AnalysisStatus.REJECTED
            any { it.status == AnalysisStatus.GREY_ZONE } -> AnalysisStatus.GREY_ZONE
            else -> AnalysisStatus.APPROVED
        }
}

data class PolicyResult(
    val id: PolicyId,
    val result: AnalysisStatus,
    val fields: List<Field>
)


data class Field(
    val type: Parameter,
    val status: AnalysisStatus,
    val sourceValues: Map<Source, Any?>
)