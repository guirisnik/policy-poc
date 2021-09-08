package com.example.policypoc.vendors.ph3a

import com.example.policypoc.vendors.ph3a.dto.Ph3aResponse
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.responseObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Ph3aService(
    @Value("\${vendors.ph3a.url}") private val url: String
) {
    fun get(document: String) : Ph3aResponse =
        Fuel
            .get("$url/$document")
            .responseObject<Ph3aResponse>()
            .third.fold({ it }, { throw it })
}