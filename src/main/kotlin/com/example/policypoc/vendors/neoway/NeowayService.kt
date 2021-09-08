package com.example.policypoc.vendors.neoway

import com.example.policypoc.vendors.neoway.dto.NeowayResponse
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.responseObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NeowayService(
    @Value("\${vendors.neoway.url}") private val url: String
) {
    fun get(document: String): NeowayResponse =
        Fuel
            .get("$url/$document")
            .responseObject<NeowayResponse>()
            .third.fold({ it }, { throw it })
}