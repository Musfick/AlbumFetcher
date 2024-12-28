package com.musfick.albumfetcher.core.utils

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy



@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExcludeFromJacocoGeneratedReport

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcludeFromCoverage