package com.wojdor.memolki.util.extension

fun <T> Collection<T>.toLinkedSet(): LinkedHashSet<T> {
    return LinkedHashSet(this)
}
