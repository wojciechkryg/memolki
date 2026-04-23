package com.wojdor.memolki.util.resource

import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString

class AppStringProvider : StringProvider {
    override suspend fun getString(resource: StringResource, vararg formatArgs: Any): String =
        getString(resource, *formatArgs)

    override suspend fun getPluralString(
        resource: PluralStringResource,
        quantity: Int,
        vararg formatArgs: Any
    ): String = getPluralString(resource, quantity, *formatArgs)
}
