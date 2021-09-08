package com.example.policypoc.adapters

import com.example.policypoc.controllers.dto.policy.Source
import com.example.policypoc.infrastructure.exceptions.MissingMandatoryParameterException
import com.example.policypoc.infrastructure.exceptions.SourceNotFoundException
import com.example.policypoc.vendors.neoway.NeowayService
import com.example.policypoc.vendors.ph3a.Ph3aService
import org.springframework.stereotype.Service
import kotlin.reflect.full.valueParameters

@Service
class PersonalDataAdapter(
    private val neowayService: NeowayService,
    private val ph3aService: Ph3aService
) {
    fun execute(source: Source, params: Map<String, Any>): PersonalData =
        when (source) {
            Source.PH3A -> executePh3a(params)
            Source.NEOWAY -> executeNeoway(params)
            else -> throw SourceNotFoundException("Source $source is not implemented.")
        }


    private fun executePh3a(params: Map<String, Any>): PersonalData {
        if (ph3aService::get.valueParameters.any { !params.containsKey(it.name) }) throw MissingMandatoryParameterException("Missing parameter for PH3A execution.")
        return ph3aService::get
            .valueParameters
            .associateWith { params[it.name] }
            .let(ph3aService::get::callBy)
            .let(::PersonalData)
    }

    private fun executeNeoway(params: Map<String, Any>): PersonalData {
        if (neowayService::get.valueParameters.any { !params.containsKey(it.name) }) throw MissingMandatoryParameterException(
            "Missing parameter for PH3A execution."
        )
        return neowayService::get
            .valueParameters
            .associateWith { params[it.name] }
            .let(neowayService::get::callBy)
            .let(::PersonalData)
    }
}