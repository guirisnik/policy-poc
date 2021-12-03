package com.example.policypoc.controllers.dto.policy

import com.example.policypoc.adapters.DocumentStatus
import com.example.policypoc.adapters.PersonalData
import com.example.policypoc.services.validation.AnalysisStatus
import com.example.policypoc.services.validation.Field
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.apache.commons.text.similarity.LevenshteinDistance

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = NameValidation::class, name = "NAME"),
    JsonSubTypes.Type(value = DocumentValidation::class, name = "DOCUMENT"),
    JsonSubTypes.Type(value = PhoneValidation::class, name = "PHONE"),
//    JsonSubTypes.Type(value = AddressValidation::class, name = "ADDRESS")
)
interface Validatable {
    val type: Parameter
    val sources: Set<Source>
    val isDecisive: Boolean
    val decision: Decision?
        get() = null
}

interface Decision

@JsonTypeName("NAME")
data class NameValidation(
    override val type: Parameter,
    override val sources: Set<Source>,
    override val isDecisive: Boolean,
    override val decision: NameDecision
) : Validatable {
    data class NameDecision(
        val approve: Double,
        val reject: Double
    ) : Decision

    private fun NameDecision.isApproved(score: Double): Boolean = score >= approve
    private fun NameDecision.isRejected(score: Double): Boolean = score <= reject

    private fun validate(source: String, target: String): AnalysisStatus =
        maxOf(source.length, target.length)
            .toDouble()
            .let { it.minus(LevenshteinDistance().apply(source, target)).div(it).times(100) }
            .let {
                when {
                    decision.isApproved(it) -> AnalysisStatus.APPROVED
                    decision.isRejected(it) -> AnalysisStatus.REJECTED
                    else -> AnalysisStatus.GREY_ZONE
                }
            }

    fun toField(
        sourceValues: Map<Source, PersonalData>,
        target: PersonalData
    ) = Field(
        type = Parameter.NAME,
        status = sources
            .map { validate(sourceValues[it]!!.name, target.name) }
            .converge(),
        sourceValues = sources.associateWith { sourceValues[it]!!.name }
    )
}

@JsonTypeName("DOCUMENT")
data class DocumentValidation(
    override val type: Parameter,
    override val sources: Set<Source>,
    override val isDecisive: Boolean,
    override val decision: DocumentDecision
) : Validatable {
    data class DocumentDecision(
        val approve: Set<DocumentStatus>,
        val reject: Set<DocumentStatus>
    ) : Decision

    private fun DocumentDecision.isApproved(status: DocumentStatus): Boolean = approve.contains(status)
    private fun DocumentDecision.isRejected(status: DocumentStatus): Boolean = reject.contains(status)

    private fun validate(status: DocumentStatus): AnalysisStatus =
        when {
            decision.isApproved(status) -> AnalysisStatus.APPROVED
            decision.isRejected(status) -> AnalysisStatus.REJECTED
            else -> AnalysisStatus.GREY_ZONE
        }

    fun toField(
        sourceValues: Map<Source, PersonalData>
    ) = Field(
        type = Parameter.DOCUMENT,
        status = sources
            .map { validate(sourceValues[it]!!.document.status) }
            .converge(),
        sourceValues = sources.associateWith { sourceValues[it]!!.document.status }
    )
}

@JsonTypeName("PHONE")
data class PhoneValidation(
    override val type: Parameter,
    override val sources: Set<Source>,
    override val isDecisive: Boolean
) : Validatable {
    private fun isApproved(input: String, phones: List<String>): Boolean = phones.contains(input)

    private fun validate(input: String, phones: List<String>): AnalysisStatus = when {
        isApproved(input, phones) -> AnalysisStatus.APPROVED
        else -> AnalysisStatus.REJECTED
    }

    fun toField(
        sourceValues: Map<Source, PersonalData>,
        target: PersonalData
    ) = Field(
        type = Parameter.PHONE,
        status = sources.map { validate(
            target.phones!!.first(),
            sourceValues[it]!!.phones!!
        ) }.converge(),
        sourceValues = sources.associateWith { sourceValues[it]!!.phones }
    )
}

//@JsonTypeName("ADDRESS")
//data class AddressValidation(
//    override val type: Parameter,
//    override val sources: List<Source>,
//    override val isDecisive: Boolean
//) : Validatable

//class ContactToAnonymize(
//    override val name: String,
//    override val model: String,
//    val type: ContactType,
//    val channel: ChannelType,
//    val code: String
//) : DataToAnonymize<Contact>(name, model) {
//    override fun toAnonymizable(): Contact = Contact(channel, type, code)
//}
//
//class MainDocumentToAnonymize(
//    override val name: String,
//    override val model: String,
//    val type: MainDocumentType,
//    val code: String
//) : DataToAnonymize<MainDocument>(name, model) {
//    override fun toAnonymizable(): MainDocument = MainDocument(type, code)
//}
//
//class DocumentToAnonymize(
//    override val name: String,
//    override val model: String,
//    val type: DocumentType,
//    val code: String
//) : DataToAnonymize<Document>(name, model) {
//    override fun toAnonymizable(): Document = Document(type, code)
//}
//
//@Suppress("LongParameterList")
//class AddressToAnonymize(
//    override val name: String,
//    override val model: String,
//    val type: AddressType,
//    val country: String,
//    val administrativeAreaLevel1: String? = null,
//    val administrativeAreaLevel2: String? = null,
//    val administrativeAreaLevel3: String? = null,
//    val administrativeAreaLevel4: String? = null,
//    val administrativeAreaLevel5: String? = null,
//    val neighborhood: String? = null,
//    val street: String? = null,
//    val number: String? = null,
//    val complement: String? = null,
//    val zipCode: String? = null,
//) : DataToAnonymize<Address>(name, model) {
//    override fun toAnonymizable(): Address = Address(
//        type,
//        country,
//        administrativeAreaLevel1,
//        administrativeAreaLevel2,
//        administrativeAreaLevel3,
//        administrativeAreaLevel4,
//        administrativeAreaLevel5,
//        neighborhood,
//        street,
//        number,
//        complement,
//        zipCode
//    )
//}
//
//class OtherInformationToAnonymize(
//    override val name: String,
//    override val model: String,
//    val purpose: String,
//    val value: String,
//    val hashLength: Int,
//) : DataToAnonymize<OtherInformation>(name, model) {
//    override fun toAnonymizable(): OtherInformation =
//        OtherInformation(purpose, value, hashLength)
//}
//
//class VehicleInformationToAnonymize(
//    override val name: String,
//    override val model: String,
//    val type: VehicleInformationType,
//    val code: String,
//) : DataToAnonymize<VehicleInformation>(name, model) {
//    override fun toAnonymizable(): VehicleInformation =
//        VehicleInformation(type, code)
//}

fun List<AnalysisStatus>.converge() =
    when {
        any { it == AnalysisStatus.APPROVED } -> AnalysisStatus.APPROVED
        any { it == AnalysisStatus.GREY_ZONE } -> AnalysisStatus.GREY_ZONE
        else -> AnalysisStatus.REJECTED
    }