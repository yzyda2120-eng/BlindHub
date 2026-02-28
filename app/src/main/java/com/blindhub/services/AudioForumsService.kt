package com.blindhub.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log

interface AudioForumsService {
    fun enterForums()
    fun postAudioMessage(message: String)
    fun listenToForums()
}

class AudioForumsServiceImpl(private val context: Context, private val tts: TextToSpeech) : AudioForumsService {
    private val TAG = "AudioForumsService"

    override fun enterForums() {
        Log.d(TAG, "Entering audio forums...")
        tts.speak("أهلاً بك في المنتديات الصوتية. يمكنك قول 'نشر رسالة' أو 'الاستماع للمنتديات'.", TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun postAudioMessage(message: String) {
        Log.d(TAG, "Posting audio message: $message")
        tts.speak("تم نشر رسالتك الصوتية بنجاح.", TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun listenToForums() {
        Log.d(TAG, "Listening to audio forums...")
        tts.speak("جارٍ تشغيل أحدث الرسائل في المنتديات الصوتية.", TextToSpeech.QUEUE_FLUSH, null, "")
    }
}
