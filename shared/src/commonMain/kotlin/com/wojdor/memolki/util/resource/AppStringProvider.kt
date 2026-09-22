package com.wojdor.memolki.util.resource

import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString as composeGetPluralString
import org.jetbrains.compose.resources.getString as composeGetString
import org.jetbrains.compose.resources.getStringArray as composeGetStringArray

class AppStringProvider : StringProvider {
    override suspend fun getString(resource: StringResource, vararg formatArgs: Any): String =
        composeGetString(resource, *formatArgs)

    override suspend fun getPluralString(
        resource: PluralStringResource,
        quantity: Int,
        vararg formatArgs: Any
    ): String = composeGetPluralString(resource, quantity, *formatArgs)

    override suspend fun getStringArray(resource: StringArrayResource): List<String> =
        composeGetStringArray(resource)
}
