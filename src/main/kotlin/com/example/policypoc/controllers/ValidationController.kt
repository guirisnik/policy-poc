package com.example.policypoc.controllers

import com.example.policypoc.controllers.dto.validation.ValidationRequest
import com.example.policypoc.services.validation.ValidationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class ValidationController(
    private val validationService: ValidationService
) {
    @GetMapping("/{policyId}")
    fun get(@PathVariable policyId: UUID, @RequestBody request: ValidationRequest) : ResponseEntity<Void> {
        validationService.validate(policyId, request)
        return ResponseEntity.ok(null)
    }
}