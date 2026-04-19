package com.wojdor.memolki.util.extension

val String.containsWhitespace: Boolean
    get() = this.any { it.isWhitespace() }
