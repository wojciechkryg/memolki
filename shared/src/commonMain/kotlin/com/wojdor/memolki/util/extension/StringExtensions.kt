package com.wojdor.memolki.util.extension

val String.containsWhitespace: Boolean
    get() = any { it.isWhitespace() }
