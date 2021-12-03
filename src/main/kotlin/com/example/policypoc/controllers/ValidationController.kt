package com.example.policypoc.controllers

import com.example.policypoc.controllers.dto.validation.ValidationRequest
import com.example.policypoc.data.models.PolicyId
import com.example.policypoc.services.validation.PolicyResult
import com.example.policypoc.services.validation.ValidationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ValidationController(
    private val validationService: ValidationService
) {
    @PostMapping("/{policyId}")
    fun validate(@PathVariable policyId: PolicyId, @RequestBody request: ValidationRequest) : ResponseEntity<PolicyResult> {
        return ResponseEntity.ok(validationService.validate(policyId, request))
    }
}