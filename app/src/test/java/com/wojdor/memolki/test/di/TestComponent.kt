package com.wojdor.memolki.test.di

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
@Component(modules = [TestModule::class, TestCoroutineModule::class])
interface TestComponent : TestInjector
