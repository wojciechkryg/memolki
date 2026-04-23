package com.wojdor.memolki.util.resource

import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

interface StringProvider {
    suspend fun getString(resource: StringResource, vararg formatArgs: Any): String
    suspend fun getPluralString(
        resource: PluralStringResource,
        quantity: Int,
        vararg formatArgs: Any
    ): String
}
