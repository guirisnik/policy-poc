package com.example.policypoc.vendors.ph3a.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

data class Ph3aResponse(
    val data: Data
) {
    data class Data(
        @JsonProperty("nome_completo")
        val name: String,
        @JsonProperty("documento")
        val document: Document,
        @JsonProperty("endereco")
        val address: Address,
        val email: String,
        @JsonProperty("telefone")
        val phone: String
    )

    data class Document(
        @JsonProperty("tipo")
        val type: String,
        @JsonProperty("numero")
        val code: String,
        @JsonProperty("situacaoCadastral")
        val status: Status
    ) {
        enum class Status(@JsonValue val status: String) {
            ACTIVE("REGULAR"),
            CANCELLED("CANCELADO"),
            SUSPENDED("SUSPENSO"),
            IRREGULAR("IRREGULAR"),
            NULL("NULO"),
            DEAD("ÓBITO")
        }
    }

    data class Address(
        @JsonProperty("rua")
        val street: String,
        @JsonProperty("cep")
        val zipCode: String,
        @JsonProperty("cidade")
        val city: String,
        @JsonProperty("estado")
        val state: String,
        @JsonProperty("numero")
        val number: String
    )
}
