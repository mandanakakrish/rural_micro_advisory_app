package com.ruraladvisory.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.ruraladvisory.advisory.knowledge.MultilingualDictionary
import com.ruraladvisory.advisory.model.FeasibilityReport
import com.ruraladvisory.advisory.model.LanguageCode
import com.ruraladvisory.finance.model.FinancialStructure
import com.ruraladvisory.finance.model.SchemeCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

/**
 * High-reliability Android Text-to-Speech assistant providing spoken audio
 * summaries of feasibility recommendations and loan structuring in English and Hindi.
 */
class TextToSpeechHelper(
    private val context: Context,
    private val onInitComplete: ((Boolean) -> Unit)? = null
) : TextToSpeech.OnInitListener {

    companion object {
        private const val TAG = "TextToSpeechHelper"
        private const val UTTERANCE_ID = "RURAL_ADVISORY_SUMMARY"

        /**
         * Formats double values into clean Indian currency format (e.g. 1,40,000).
         */
        fun formatIndianCurrency(amount: Double): String {
            val longVal = kotlin.math.round(amount).toLong()
            val isNegative = longVal < 0
            val absStr = kotlin.math.abs(longVal).toString()
            if (absStr.length <= 3) {
                return if (isNegative) "-$absStr" else absStr
            }
            val last3 = absStr.takeLast(3)
            val remaining = absStr.dropLast(3)
            val formattedRemaining = remaining.reversed().chunked(2).joinToString(",").reversed()
            val formatted = "$formattedRemaining,$last3"
            return if (isNegative) "-$formatted" else formatted
        }

        /**
         * Synthesizes a natural, concise spoken advisory script tailored for rural audio playback.
         */
        fun buildSpokenScript(
            report: FeasibilityReport,
            structure: FinancialStructure?,
            scheme: SchemeCategory?,
            language: LanguageCode
        ): String {
            val categoryTitle = MultilingualDictionary.getCategoryTitle(report.category, language)
            val locationFormatted = report.location.formatted(language)

            val costStr = structure?.let { formatIndianCurrency(it.totalProjectCost) } ?: "N/A"
            val loanStr = structure?.let { formatIndianCurrency(it.actualLoanAmount) } ?: "N/A"
            val rateStr = scheme?.let { String.format(Locale.US, "%.1f", it.annualInterestRate * 100.0) } ?: "7.0"
            val moratoriumMonths = scheme?.moratoriumMonths?.toString() ?: "3"

            val topStrength = report.swotAnalysis.strengths.firstOrNull() ?: ""
            val topOpportunity = report.swotAnalysis.opportunities.firstOrNull() ?: ""
            val topThreat = report.threatsIdentification.firstOrNull() ?: ""
            val topRec = report.keyRecommendations.firstOrNull() ?: ""

            return if (language == LanguageCode.HI) {
                val schemeName = if (scheme == SchemeCategory.MICRO_FINANCE) {
                    "सूक्ष्म वित्त योजना"
                } else {
                    "सावधि ऋण योजना"
                }
                buildString {
                    append("$locationFormatted में $categoryTitle के लिए सलाह। ")
                    append("$schemeName के तहत, कुल परियोजना लागत ₹$costStr है, ")
                    append("जिसमें $rateStr प्रतिशत वार्षिक ब्याज और $moratoriumMonths माह की छूट अवधि के साथ ₹$loanStr का रियायती ऋण प्राप्त हो सकता है। ")
                    if (topStrength.isNotBlank()) {
                        append("मुख्य ताकत: $topStrength। ")
                    }
                    if (topOpportunity.isNotBlank()) {
                        append("प्रमुख अवसर: $topOpportunity। ")
                    }
                    if (topThreat.isNotBlank()) {
                        append("सुरक्षा सुझाव: $topThreat। ")
                    }
                    if (topRec.isNotBlank()) {
                        append("रणनीतिक परामर्श: $topRec। ")
                    }
                    append("धन्यवाद।")
                }
            } else {
                val schemeName = if (scheme == SchemeCategory.MICRO_FINANCE) {
                    "Micro Finance Scheme"
                } else {
                    "Term Loan Scheme"
                }
                buildString {
                    append("Advisory summary for $categoryTitle in $locationFormatted. ")
                    append("Under the $schemeName, your total project cost is ₹$costStr, ")
                    append("qualifying for a concessional loan of ₹$loanStr at $rateStr percent annual interest with a $moratoriumMonths month moratorium. ")
                    if (topStrength.isNotBlank()) {
                        append("Top strength: $topStrength. ")
                    }
                    if (topOpportunity.isNotBlank()) {
                        append("Key opportunity: $topOpportunity. ")
                    }
                    if (topThreat.isNotBlank()) {
                        append("Risk notice: $topThreat. ")
                    }
                    if (topRec.isNotBlank()) {
                        append("Primary recommendation: $topRec. ")
                    }
                    append("Thank you.")
                }
            }
        }
    }

    enum class TtsStatus {
        IDLE,
        INITIALIZING,
        READY,
        SPEAKING,
        ERROR
    }

    private var tts: TextToSpeech? = null

    private val _status = MutableStateFlow(TtsStatus.IDLE)
    val status: StateFlow<TtsStatus> = _status.asStateFlow()

    private val _lastMessage = MutableStateFlow<String?>(null)
    val lastMessage: StateFlow<String?> = _lastMessage.asStateFlow()

    init {
        initialize()
    }

    fun initialize() {
        if (tts != null) return
        _status.value = TtsStatus.INITIALIZING
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to instantiate TextToSpeech", e)
            _status.value = TtsStatus.ERROR
            _lastMessage.value = e.message
            onInitComplete?.invoke(false)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            _status.value = TtsStatus.READY
            setupProgressListener()
            onInitComplete?.invoke(true)
        } else {
            Log.e(TAG, "TextToSpeech init failed with code: $status")
            _status.value = TtsStatus.ERROR
            _lastMessage.value = "TTS initialization failed (code $status)"
            onInitComplete?.invoke(false)
        }
    }

    private fun setupProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _status.value = TtsStatus.SPEAKING
            }

            override fun onDone(utteranceId: String?) {
                _status.value = TtsStatus.READY
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _status.value = TtsStatus.READY
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                Log.e(TAG, "TTS utterance error: $errorCode")
                _status.value = TtsStatus.READY
            }
        })
    }

    /**
     * Speaks the given [text] in the specified [language].
     * Falls back to English if Hindi voice data is missing on the device.
     */
    fun speak(
        text: String,
        language: LanguageCode = LanguageCode.EN,
        onDone: (() -> Unit)? = null
    ): Boolean {
        val engine = tts ?: run {
            _status.value = TtsStatus.ERROR
            return false
        }

        val targetLocale = if (language == LanguageCode.HI) {
            Locale("hi", "IN")
        } else {
            Locale.ENGLISH
        }

        var available = engine.isLanguageAvailable(targetLocale)
        var usedLocale = targetLocale

        if (available == TextToSpeech.LANG_MISSING_DATA || available == TextToSpeech.LANG_NOT_SUPPORTED) {
            if (language == LanguageCode.HI) {
                Log.w(TAG, "Hindi voice missing, falling back to English")
                _lastMessage.value = "Hindi voice data not installed on device. Falling back to English voice."
                usedLocale = Locale.ENGLISH
                available = engine.isLanguageAvailable(usedLocale)
            }
        }

        if (available == TextToSpeech.LANG_MISSING_DATA || available == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(TAG, "Locale $usedLocale is not supported on this device")
            _status.value = TtsStatus.ERROR
            _lastMessage.value = "Speech synthesis language not supported on device."
            return false
        }

        engine.language = usedLocale

        val result = engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)
        return result == TextToSpeech.SUCCESS
    }

    fun stop() {
        try {
            tts?.stop()
            _status.value = TtsStatus.READY
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping TTS", e)
        }
    }

    fun isSpeaking(): Boolean {
        return tts?.isSpeaking == true || _status.value == TtsStatus.SPEAKING
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            _status.value = TtsStatus.IDLE
        } catch (e: Exception) {
            Log.e(TAG, "Error shutting down TTS", e)
        }
    }
}
