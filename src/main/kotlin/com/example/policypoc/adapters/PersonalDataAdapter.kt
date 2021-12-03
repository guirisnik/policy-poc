package com.example.policypoc.adapters

import com.example.policypoc.controllers.dto.policy.Source
import com.example.policypoc.infrastructure.exceptions.SourceNotFoundException
import com.example.policypoc.vendors.neoway.NeowayService
import com.example.policypoc.vendors.ph3a.Ph3aService
import org.springframework.stereotype.Service

@Service
class PersonalDataAdapter(
    private val neowayService: NeowayService,
    private val ph3aService: Ph3aService
) {
    fun execute(source: Source, personalData: PersonalData): PersonalData =
        when (source) {
            Source.PH3A -> executePh3a(personalData)
            Source.NEOWAY -> executeNeoway(personalData)
            else -> throw SourceNotFoundException("Source $source is not implemented.")
        }


    private fun executePh3a(personalData: PersonalData): PersonalData =
        ph3aService.get(personalData.document.code)
            .let(::PersonalData)


    private fun executeNeoway(personalData: PersonalData): PersonalData =
        neowayService.get(personalData.document.code)
            .let(::PersonalData)
}