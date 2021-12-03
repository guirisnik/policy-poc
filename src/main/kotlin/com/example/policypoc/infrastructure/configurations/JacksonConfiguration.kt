package com.example.policypoc.infrastructure.configurations

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import com.fasterxml.jackson.databind.ser.SerializerFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.text.SimpleDateFormat

@Configuration
class JacksonConfiguration {

    @Bean
    @Primary
    fun jackson2ObjectMapperBuilder(): ObjectMapper {
        val mapper = Jackson2ObjectMapperBuilder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .dateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"))
            .build<ObjectMapper>()
        mapper.setSerializerProvider(
            CustomSerializerProvider(
                mapper.serializerProvider,
                mapper.serializationConfig,
                mapper.serializerFactory
            )
        )
        return mapper.registerModule(KotlinModule())
    }

    @Bean
    fun jackson2ObjectMapperBuilderSnakeCase(): Jackson2ObjectMapperBuilder {
        return Jackson2ObjectMapperBuilder()
            .propertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
            .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .dateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"))
    }
}

class NullCollectionSerializer : JsonSerializer<Any?>() {
    override fun serialize(
        value: Any?,
        gen: JsonGenerator?,
        serializers: SerializerProvider?
    ) {
        gen?.writeStartArray()
        gen?.writeEndArray()
    }
}

class CustomSerializerProvider(
    src: SerializerProvider,
    configs: SerializationConfig?,
    f: SerializerFactory?
) : DefaultSerializerProvider(src, configs, f) {
    override fun createInstance(
        config: SerializationConfig?,
        jsf: SerializerFactory?
    ): DefaultSerializerProvider {
        return CustomSerializerProvider(
            this,
            config,
            jsf
        )
    }

    @Throws(JsonMappingException::class)
    override fun findNullValueSerializer(property: BeanProperty?): JsonSerializer<Any?>? {
        if (property?.type?.isTypeOrSubTypeOf(Collection::class.java)!!) {
            return NullCollectionSerializer()
        }
        return super.findNullValueSerializer(property)
    }
}