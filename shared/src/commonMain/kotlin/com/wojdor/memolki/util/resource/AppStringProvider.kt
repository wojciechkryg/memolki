package com.wojdor.memolki.util.resource

import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.getStringArray

class AppStringProvider : StringProvider {
    override suspend fun getString(resource: StringResource, vararg formatArgs: Any): String =
        getString(resource, *formatArgs)

    override suspend fun getPluralString(
        resource: PluralStringResource,
        quantity: Int,
        vararg formatArgs: Any
    ): String = getPluralString(resource, quantity, *formatArgs)

    override suspend fun getStringArray(resource: StringArrayResource): List<String> =
        getStringArray(resource)
}
