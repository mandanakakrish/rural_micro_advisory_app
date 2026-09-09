package com.ruraladvisory.advisory.model

/**
 * Supported vernacular language codes for the advisory engine.
 */
enum class LanguageCode(val code: String, val displayName: String) {
    EN("en", "English"),
    HI("hi", "हिंदी")
}
