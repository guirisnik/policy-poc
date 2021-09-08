package com.example.policypoc.vendors.neoway.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

data class NeowayResponse(
    val data: Data
) {
    data class Data(
        @JsonProperty("informacoesPessoais")
        val personalInformation: PersonalInformation,
        @JsonProperty("situacaoDoDocumento")
        val document: Document
    ) {
        data class PersonalInformation(
            @JsonProperty("nome")
            val name: String,
            @JsonProperty("cpf")
            val documentCode: String,
            @JsonProperty("endereco")
            val address: Address,
            val email: String,
            @JsonProperty("telefone")
            val phone: String
        ) {
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

        data class Document(
            @JsonProperty("cpf")
            val code: String,
            @JsonProperty("cadastro")
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
    }
}
