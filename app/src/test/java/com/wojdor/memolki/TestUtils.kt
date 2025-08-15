package com.wojdor.memolki

import io.mockk.MockKVerificationScope
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify

inline fun <reified T : Any> relaxedMockk(name: String? = null, block: T.() -> Unit = {}): T =
    mockk(name, relaxed = true, relaxUnitFun = true, block = block)

fun verifyOnce(verifyBlock: MockKVerificationScope.() -> Unit) =
    verify(exactly = 1, verifyBlock = verifyBlock)

fun coVerifyOnce(verifyBlock: suspend MockKVerificationScope.() -> Unit) =
    coVerify(exactly = 1, verifyBlock = verifyBlock)
