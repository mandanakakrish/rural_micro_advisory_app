package com.ruraladvisory.advisory.model

enum class ChatSender {
    USER,
    ASSISTANT
}

data class ChatMessage(
    val id: String,
    val sender: ChatSender,
    val text: String,
    val timestamp: String,
    val isSpeaking: Boolean = false
)
