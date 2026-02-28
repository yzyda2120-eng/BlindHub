package com.blindhub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.speech.tts.TextToSpeech
import android.speech.RecognizerIntent
import android.content.Intent
import android.util.Log
import java.util.Locale
import com.blindhub.VoiceCommandProcessor

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private val SPEECH_REQUEST_CODE = 100
    private lateinit var voiceCommandProcessor: VoiceCommandProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        // voiceCommandProcessor will be initialized in onInit after TTS is ready
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "اللغة غير مدعومة")
            } else {
                voiceCommandProcessor = VoiceCommandProcessor(this, tts!!)
                speak("مرحباً بك في BlindHub. قل مساعدة للحصول على قائمة الأوامر.")
            }
        } else {
            Log.e("TTS", "فشل التهيئة")
        }
    }

    private fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "تحدث الآن...")
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: Exception) {
            Log.e("STT", "فشل بدء التعرف على الكلام: ${e.message}")
            speak("عذراً، لا يمكنني بدء التعرف على الكلام في الوقت الحالي.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                val spokenText = matches[0]
                processVoiceCommand(spokenText)
            }
        }
    }

    private fun processVoiceCommand(command: String) {
        voiceCommandProcessor.processCommand(command) {
            // Optional: handle post-command processing if needed
        }
    }

    override fun onDestroy() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
    }

    // Placeholder for UI layout - will be mostly empty as it's voice-driven
    // You would typically have a minimal layout with perhaps a background or a logo
}
