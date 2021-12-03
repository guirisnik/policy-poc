package com.example.policypoc.infrastructure.configurations

import com.example.policypoc.data.models.PolicyIdToStringConverter
import com.example.policypoc.data.models.StringToPolicyIdConverter
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.context.support.beans

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
internal class WebConfiguration : ApplicationContextInitializer<GenericApplicationContext> {
    private fun beans() = beans { bean { mongoCustomConversions() } }

    override fun initialize(context: GenericApplicationContext) = beans().initialize(context)

    private fun mongoCustomConversions(): MongoCustomConversions {
        return MongoCustomConversions(
            listOf(
                PolicyIdToStringConverter(),
                StringToPolicyIdConverter()
            )
        )
    }
}
