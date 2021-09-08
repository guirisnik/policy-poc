package com.example.policypoc.controllers.dto.validation

import com.example.policypoc.adapters.PersonalData

data class ValidationRequest(
    val params: Map<String, Any>,
    val personalData: PersonalData
)
