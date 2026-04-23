package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.resource.StringProvider
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

class FakeStringProvider : StringProvider {

    override suspend fun getString(resource: StringResource, vararg formatArgs: Any): String {
        val key = resource.key
        return if (formatArgs.isEmpty()) key else "$key(${formatArgs.joinToString()})"
    }

    override suspend fun getPluralString(
        resource: PluralStringResource,
        quantity: Int,
        vararg formatArgs: Any
    ): String {
        val key = resource.key
        val args = if (formatArgs.isEmpty()) "" else "(${formatArgs.joinToString()})"
        return "$key[$quantity]$args"
    }
}
