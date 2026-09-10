package com.ruraladvisory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruraladvisory.advisory.engine.RuralAdvisorChatEngine
import com.ruraladvisory.advisory.model.ChatMessage
import com.ruraladvisory.advisory.model.ChatSender
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.audio.TextToSpeechHelper
import com.ruraladvisory.ui.theme.AgriTealSecondary
import com.ruraladvisory.ui.theme.ForestGreenDark
import com.ruraladvisory.ui.theme.ForestGreenLight
import com.ruraladvisory.ui.theme.ForestGreenPrimary
import com.ruraladvisory.ui.theme.OutlineLight
import androidx.compose.material.icons.filled.Stop
import com.ruraladvisory.ui.theme.SurfaceCard
import com.ruraladvisory.ui.theme.SurfaceCream
import com.ruraladvisory.ui.theme.SwotStrengthGreenBg
import com.ruraladvisory.ui.theme.TextHighContrast
import com.ruraladvisory.ui.theme.TextMediumContrast
import com.ruraladvisory.ui.theme.TextMuted
import com.ruraladvisory.ui.theme.WarmCreamBackground
import com.ruraladvisory.ui.viewmodel.AdvisoryUiState
import com.ruraladvisory.ui.viewmodel.AdvisoryViewModel

/**
 * Screen 5: AI Rural Sahayak Conversational Advisor
 * Reference: WEB/src/components/AdvisorView.tsx & ChatAdvisorModal.tsx
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdvisorChatScreen(
    uiState: AdvisoryUiState,
    viewModel: AdvisoryViewModel,
    ttsHelper: TextToSpeechHelper,
    onStartVoiceInput: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val isHindi = uiState.language == LanguageCode.HI
    val samplePrompts = remember(uiState.language) { RuralAdvisorChatEngine.getSamplePrompts(uiState.language) }

    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isImeVisible) {
        if (isImeVisible && uiState.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chatMessages.size - 1)
        }
    }

    LaunchedEffect(uiState.chatMessages.size) {
        if (uiState.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .imePadding()
    ) {
        // Chat Header Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCream),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, OutlineLight)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ForestGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = SurfaceCream,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isHindi) "उद्यम सहायक" else "ABHYUDAY Advisor",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(ForestGreenLight.copy(alpha = 0.5f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (uiState.apiKey.isNotBlank()) {
                                    if (isHindi) "एआई सक्रिय" else "AI Active"
                                } else {
                                    if (isHindi) "ऑफ़लाइन" else "Offline AI"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = ForestGreenDark,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                    Text(
                        text = "${uiState.customCategory.ifBlank { uiState.selectedCategory.displayName(uiState.language) }} • ${uiState.village.ifBlank { uiState.district }}, ${uiState.state}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Quick Suggestion Prompt Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(samplePrompts) { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceCream)
                        .border(0.5.dp, ForestGreenPrimary.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                        .clickable {
                            viewModel.sendChatMessage(prompt)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.bodySmall,
                        color = ForestGreenDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Chat Message Bubbles List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(uiState.chatMessages, key = { it.id }) { msg ->
                ChatBubbleItem(
                    message = msg,
                    language = uiState.language,
                    ttsHelper = ttsHelper
                )
            }

            if (uiState.isChatSending) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceCream)
                                .border(0.5.dp, OutlineLight, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = ForestGreenPrimary
                                )
                                Text(
                                    text = if (isHindi) "सलाहकार सोच रहा है..." else "Sahayak is typing...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Chat Input Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCream),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, OutlineLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = if (isHindi) "अपना सवाल यहाँ पूछें..." else "Ask a business query...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedBorderColor = ForestGreenPrimary,
                        unfocusedBorderColor = OutlineLight
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendChatMessage(inputText)
                                inputText = ""
                            }
                        }
                    )
                )

                // Voice input mic
                IconButton(
                    onClick = onStartVoiceInput,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SwotStrengthGreenBg)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = ForestGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Send button
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendChatMessage(inputText)
                            inputText = ""
                        }
                    },
                    enabled = inputText.isNotBlank() && !uiState.isChatSending,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) ForestGreenPrimary else OutlineLight)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = SurfaceCream,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(
    message: ChatMessage,
    language: LanguageCode,
    ttsHelper: TextToSpeechHelper
) {
    val isUser = message.sender == ChatSender.USER
    var isSpeaking by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 2.dp,
                            bottomEnd = if (isUser) 2.dp else 16.dp
                        )
                    )
                    .background(if (isUser) ForestGreenPrimary else SurfaceCream)
                    .border(0.5.dp, if (isUser) ForestGreenPrimary else OutlineLight, RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isUser) SurfaceCream else TextHighContrast,
                        lineHeight = 20.sp,
                        fontSize = 13.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = message.timestamp,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUser) SurfaceCream.copy(alpha = 0.7f) else TextMuted,
                            fontSize = 9.sp
                        )

                        if (!isUser) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        if (isSpeaking) {
                                            ttsHelper.stop()
                                            isSpeaking = false
                                        } else {
                                            val ok = ttsHelper.speak(message.text, language)
                                            if (ok) isSpeaking = true
                                        }
                                    }
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                    contentDescription = "Speak",
                                    tint = ForestGreenDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (isSpeaking) "Stop" else "Listen",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ForestGreenDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
