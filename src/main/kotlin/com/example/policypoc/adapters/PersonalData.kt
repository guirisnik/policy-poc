package com.example.policypoc.adapters

import com.example.policypoc.vendors.neoway.dto.NeowayResponse
import com.example.policypoc.vendors.ph3a.dto.Ph3aResponse

data class PersonalData(
    val name: String,
    val document: Document,
    val addresses: List<Address>? = emptyList(),
    val emails: List<String>? = emptyList(),
    val phones: List<String>? = emptyList()
) {
    constructor(ph3aResponse: Ph3aResponse) : this(
        name = ph3aResponse.data.name,
        document = ph3aResponse.let(::Document),
        addresses = listOf(ph3aResponse.let(::Address)),
        emails = listOf(ph3aResponse.data.email),
        phones = listOf(ph3aResponse.data.phone)
    )

    constructor(neowayResponse: NeowayResponse) : this(
        name = neowayResponse.data.personalInformation.name,
        document = neowayResponse.let(::Document),
        addresses = listOf(neowayResponse.let(::Address)),
        emails = listOf(neowayResponse.data.personalInformation.email),
        phones = listOf(neowayResponse.data.personalInformation.phone)
    )
}

data class Document(
    val type: String? = null,
    val code: String,
    val status: DocumentStatus
) {
    constructor(ph3aResponse: Ph3aResponse) : this(
        type = ph3aResponse.data.document.type,
        code = ph3aResponse.data.document.code,
        status = when (ph3aResponse.data.document.status) {
            Ph3aResponse.Document.Status.ACTIVE -> DocumentStatus.ACTIVE
            Ph3aResponse.Document.Status.CANCELLED -> DocumentStatus.CANCELLED
            Ph3aResponse.Document.Status.SUSPENDED -> DocumentStatus.SUSPENDED
            Ph3aResponse.Document.Status.IRREGULAR -> DocumentStatus.IRREGULAR
            Ph3aResponse.Document.Status.DEAD -> DocumentStatus.DEAD
            else -> DocumentStatus.IRREGULAR
        }
    )

    constructor(neowayResponse: NeowayResponse) : this(
        code = neowayResponse.data.document.code,
        status = when (neowayResponse.data.document.status) {
            NeowayResponse.Data.Document.Status.ACTIVE -> DocumentStatus.ACTIVE
            NeowayResponse.Data.Document.Status.CANCELLED -> DocumentStatus.CANCELLED
            NeowayResponse.Data.Document.Status.SUSPENDED -> DocumentStatus.SUSPENDED
            NeowayResponse.Data.Document.Status.IRREGULAR -> DocumentStatus.IRREGULAR
            NeowayResponse.Data.Document.Status.DEAD -> DocumentStatus.DEAD
            else -> DocumentStatus.IRREGULAR
        }
    )
}

enum class DocumentStatus {
    ACTIVE,
    CANCELLED,
    SUSPENDED,
    IRREGULAR,
    DEAD
}

data class Address(
    val street: String,
    val number: String,
    val zipCode: String,
    val city: String,
    val state: String,
    val complement: String? = null
) {
    constructor(ph3aResponse: Ph3aResponse) : this(
        street = ph3aResponse.data.address.street,
        number = ph3aResponse.data.address.number,
        zipCode = ph3aResponse.data.address.zipCode,
        city = ph3aResponse.data.address.city,
        state = ph3aResponse.data.address.state
    )

    constructor(neowayResponse: NeowayResponse) : this(
        street = neowayResponse.data.personalInformation.address.street,
        number = neowayResponse.data.personalInformation.address.number,
        zipCode = neowayResponse.data.personalInformation.address.zipCode,
        city = neowayResponse.data.personalInformation.address.city,
        state = neowayResponse.data.personalInformation.address.state
    )
}