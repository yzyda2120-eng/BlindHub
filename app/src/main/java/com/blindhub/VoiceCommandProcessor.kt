package com.blindhub

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import com.blindhub.services.*

class VoiceCommandProcessor(private val context: Context, private val tts: TextToSpeech) {

    private val TAG = "VoiceCommandProcessor"

    // Initialize all services
    private val audioForumsService: AudioForumsService = AudioForumsServiceImpl(context, tts)
    private val aiObjectDescriptionService: AIObjectDescriptionService = AIObjectDescriptionServiceImpl(context, tts)
    private val gpsNavigationService: GPSNavigationService = GPSNavigationServiceImpl(context, tts)
    private val sosEmergencyService: SOSEmergencyService = SOSEmergencyServiceImpl(context, tts)
    private val brailleReaderInterfaceService: BrailleReaderInterfaceService = BrailleReaderInterfaceServiceImpl(context, tts)
    private val educationalHubService: EducationalHubService = EducationalHubServiceImpl(context, tts)
    private val peerMentoringNetworkService: PeerMentoringNetworkService = PeerMentoringNetworkServiceImpl(context, tts)
    private val voiceToTextChatService: VoiceToTextChatService = VoiceToTextChatServiceImpl(context, tts)
    private val accessibleJobBoardService: AccessibleJobBoardService = AccessibleJobBoardServiceImpl(context, tts)
    private val vocalInterfaceControlService: VocalInterfaceControlService = VocalInterfaceControlServiceImpl(context, tts)

    fun processCommand(command: String, onCommandProcessed: (String) -> Unit) {
        val lowerCaseCommand = command.toLowerCase(Locale.getDefault())
        var response = ""

        when (lowerCaseCommand) {
            "مساعدة" -> response = "الأوامر المتاحة هي: المنتديات، الوصف، الملاحة، الطوارئ، برايل، التعليم، الإرشاد، الدردشة، الوظائف، التحكم. قل 'العودة' للرجوع إلى القائمة الرئيسية."
            "المنتديات" -> {
                audioForumsService.enterForums()
                response = "جارٍ فتح المنتديات الصوتية..."
            }
            "الوصف" -> {
                aiObjectDescriptionService.startObjectDescription()
                response = "جارٍ تفعيل وصف الكائنات بالذكاء الاصطناعي..."
            }
            "الملاحة" -> {
                gpsNavigationService.startNavigation("وجهة افتراضية") // Placeholder
                response = "جارٍ بدء الملاحة الصوتية..."
            }
            "الطوارئ" -> {
                sosEmergencyService.sendDistressCall()
                response = "جارٍ إرسال نداء استغاثة..."
            }
            "برايل" -> {
                brailleReaderInterfaceService.connectBrailleDisplay()
                response = "جارٍ فتح واجهة قارئ برايل..."
            }
            "التعليم" -> {
                educationalHubService.browseCourses()
                response = "جارٍ فتح مركز التعليم..."
            }
            "الإرشاد" -> {
                peerMentoringNetworkService.findMentor("أي اهتمام") // Placeholder
                response = "جارٍ البحث عن مرشدين..."
            }
            "الدردشة" -> {
                voiceToTextChatService.startChat("مستخدم افتراضي") // Placeholder
                response = "جارٍ فتح الدردشة الصوتية..."
            }
            "الوظائف" -> {
                accessibleJobBoardService.browseJobs()
                response = "جارٍ فتح لوحة الوظائف..."
            }
            "التحكم" -> {
                vocalInterfaceControlService.navigateTo("إعدادات التحكم") // Placeholder
                response = "جارٍ فتح إعدادات التحكم الصوتي..."
            }
            "العودة" -> response = "العودة إلى القائمة الرئيسية."
            else -> response = "عذراً، لم أفهم أمرك. قل مساعدة للحصول على قائمة الأوامر."
        }
        speak(response) { onCommandProcessed(response) }
    }

    private fun speak(text: String, onDone: () -> Unit) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "command_response")
        // In a real application, you would use UtteranceProgressListener to ensure onDone is called after speech completes.
        // For this example, we'll call it immediately.
        onDone()
    }
}
