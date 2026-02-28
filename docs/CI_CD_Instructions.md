# تعليمات إعداد CI/CD لتطبيق BlindHub

تهدف هذه الوثيقة إلى توفير إرشادات شاملة لإعداد بيئة تطوير متكاملة (CI/CD) لتطبيق BlindHub، بما في ذلك هيكل المشروع، وإعداد مستودع Git/GitHub، وخط أنابيب الأتمتة باستخدام GitHub Actions.

## 1. هيكل مشروع Android المتوافق مع Gradle

تم تنظيم مشروع BlindHub ليكون متوافقًا تمامًا مع نظام بناء Gradle، وهو المعيار لتطبيقات Android. يضمن هذا الهيكل سهولة البناء، وإدارة التبعيات، والتوسع المستقبلي.

```
BlindHub/
├── .github/
│   └── workflows/
│       └── build.yml               # ملف GitHub Actions CI/CD
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── blindhub/
│   │   │   │           ├── MainActivity.kt
│   │   │   │           ├── VoiceCommandProcessor.kt
│   │   │   │           └── services/          # الخدمات العشر الإلزامية
│   │   │   │               ├── AudioForumsService.kt
│   │   │   │               ├── AIObjectDescriptionService.kt
│   │   │   │               ├── GPSNavigationService.kt
│   │   │   │               ├── SOSEmergencyService.kt
│   │   │   │               ├── BrailleReaderInterfaceService.kt
│   │   │   │               ├── EducationalHubService.kt
│   │   │   │               ├── PeerMentoringNetworkService.kt
│   │   │   │               ├── VoiceToTextChatService.kt
│   │   │   │               ├── AccessibleJobBoardService.kt
│   │   │   │               └── VocalInterfaceControlService.kt
│   │   │   └── res/                  # موارد التطبيق (الرسومات، التخطيطات، القيم)
│   │   └── AndroidManifest.xml       # ملف تعريف التطبيق
│   └── build.gradle                # إعدادات بناء الوحدة (التطبيق)
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew                         # نص Gradle Wrapper لنظامي Linux/macOS
├── gradlew.bat                     # نص Gradle Wrapper لنظام Windows
├── build.gradle                    # إعدادات بناء المشروع الجذر
├── settings.gradle                 # تعريف وحدات المشروع
└── docs/
    ├── BlindOnlyVerificationLogic.md # وثيقة منطق التحقق
    ├── ProjectRoadmap.md             # خارطة طريق المشروع
    └── CI_CD_Instructions.md         # هذه الوثيقة
```

## 2. إعداد مستودع Git و GitHub

لإدارة الكود المصدري وتفعيل خط أنابيب CI/CD، ستحتاج إلى إعداد مستودع Git محلي وربطه بمستودع GitHub بعيد.

### 2.1. تهيئة مستودع Git محلي

إذا لم تكن قد قمت بذلك بالفعل، يمكنك تهيئة مستودع Git محلي داخل مجلد مشروع `BlindHub`:

```bash
cd /home/ubuntu/BlindHub
git init
```

### 2.2. إضافة الملفات والالتزام الأولي

أضف جميع ملفات المشروع إلى Git وقم بالالتزام الأولي:

```bash
git add .
git commit -m "Initial project setup for BlindHub"
```

### 2.3. إنشاء مستودع GitHub وربطه

لإنشاء مستودع GitHub خاص وربطه بمستودعك المحلي، استخدم GitHub CLI. إذا لم يكن مثبتًا، ستحتاج إلى تثبيته أولاً. بعد التثبيت، قم بتسجيل الدخول باستخدام `gh auth login`.

```bash
cd /home/ubuntu/BlindHub
gh repo create BlindHub --private --source=. --remote=origin --description="Exclusive social community app for blind users only."
```

سيقوم هذا الأمر بإنشاء مستودع خاص باسم `BlindHub` على GitHub، وربطه كمصدر بعيد `origin`، ثم يدفع الكود المصدري الحالي إليه.

### 2.4. دفع التغييرات إلى GitHub

بعد إنشاء المستودع وربطه، يمكنك دفع التغييرات من فرعك المحلي (عادةً `master` أو `main`) إلى المستودع البعيد:

```bash
git push -u origin master
```

## 3. خط أنابيب CI/CD باستخدام GitHub Actions

تم إعداد خط أنابيب CI/CD باستخدام GitHub Actions ليتم تشغيله تلقائيًا عند كل عملية دفع (push) إلى المستودع. يضمن هذا الخط أن يتم بناء التطبيق واختباره وتوفير ملف APK قابل للتثبيت بشكل مستمر.

### 3.1. ملف `build.yml`

**ملاحظة هامة**: نظرًا لقيود الأذونات، لا يمكنني دفع ملف سير عمل GitHub Actions مباشرة إلى مستودعك. ستحتاج إلى إضافة هذا الملف يدويًا إلى مستودعك على GitHub.

1.  **إنشاء الدليل**: في مستودعك على GitHub، انتقل إلى مجلد الجذر وقم بإنشاء الدلائل التالية إذا لم تكن موجودة: `.github/workflows/`.
2.  **إنشاء الملف**: داخل مجلد `workflows`، قم بإنشاء ملف جديد باسم `build.yml`.
3.  **نسخ المحتوى**: انسخ المحتوى التالي والصقه في ملف `build.yml`:

```yaml
name: Android CI

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build with Gradle
      run: ./gradlew build

    - name: Run Unit Tests
      run: ./gradlew test

    - name: Upload a debug APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk

    # Optional: Add steps for signing and releasing to Play Store
    # - name: Sign AAB
    #   run: |-
    #     echo "$KEYSTORE_BASE64" | base64 --decode > app-release-key.jks
    #     jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
    #       -keystore app-release-key.jks \
    #       app/build/outputs/bundle/release/app-release.aab \
    #       alias_name \
    #       -storepass $KEYSTORE_PASSWORD \
    #       -keypass $KEY_KEY_PASSWORD
    #   env:
    #     KEYSTORE_BASE64: ${{ secrets.KEYSTORE_BASE64 }}
    #     KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
    #     KEY_KEY_PASSWORD: ${{ secrets.KEY_KEY_PASSWORD }}

    # - name: Upload AAB to Play Store
    #   uses: r0adkll/upload-google-play@v1
    #   with:
    #     serviceAccountJson: ${{ secrets.PLAY_STORE_UPLOAD_KEY }}
    #     packageName: com.blindhub
    #     releaseFile: app/build/outputs/bundle/release/app-release.aab
    #     track: internal
    #     status: completed
```

4.  **الالتزام بالتغييرات**: قم بالالتزام بالملف الجديد مباشرة على الفرع الرئيسي (main/master) أو من خلال طلب سحب (Pull Request).

### 3.2. شرح خطوات خط الأنابيب

*   **`name: Android CI`**: اسم سير العمل.
*   **`on: [push]`**: يحدد أن سير العمل سيتم تشغيله عند كل عملية دفع إلى أي فرع في المستودع.
*   **`jobs: build`**: يحدد مهمة بناء واحدة.
    *   **`runs-on: ubuntu-latest`**: يتم تشغيل المهمة على أحدث إصدار من Ubuntu.
    *   **`steps`**: سلسلة من الإجراءات التي سيتم تنفيذها:
        *   **`actions/checkout@v3`**: يقوم بسحب الكود المصدري للمستودع إلى عامل GitHub Actions.
        *   **`Set up JDK 17`**: يقوم بإعداد بيئة Java Development Kit (JDK) الإصدار 17، وهو مطلوب لبناء تطبيقات Android الحديثة. يتم استخدام `cache: gradle` لتسريع عمليات البناء اللاحقة عن طريق تخزين تبعيات Gradle مؤقتًا.
        *   **`Grant execute permission for gradlew`**: يمنح الإذن التنفيذي لملف `gradlew` (Gradle Wrapper) لتمكين تشغيل أوامر Gradle.
        *   **`Build with Gradle`**: يقوم بتشغيل أمر `build` الخاص بـ Gradle لبناء المشروع. سيؤدي هذا إلى تجميع الكود وإنشاء ملفات APK/AAB.
        *   **`Run Unit Tests`**: يقوم بتشغيل الاختبارات الوحدوية المعرفة في المشروع باستخدام أمر `test` الخاص بـ Gradle.
        *   **`Upload a debug APK`**: يقوم بتحميل ملف `app-debug.apk` الناتج كـ artifact، والذي يمكن تنزيله من صفحة GitHub Actions بعد اكتمال سير العمل بنجاح.

**خطوات اختيارية للتوقيع والإصدار**: تتضمن التعليقات في ملف `build.yml` خطوات اختيارية لتوقيع حزمة التطبيق (AAB) وتحميلها إلى متجر Google Play. ستحتاج إلى تكوين أسرار GitHub (GitHub Secrets) لـ `KEYSTORE_BASE64` و `KEYSTORE_PASSWORD` و `KEY_KEY_PASSWORD` و `PLAY_STORE_UPLOAD_KEY` لاستخدام هذه الخطوات.

## 4. المراجع

[1] [GitHub Actions Documentation](https://docs.github.com/en/actions)
[2] [Gradle Official Documentation](https://docs.gradle.org/current/userguide/userguide.html)
[3] [Android Developers - Build your app](https://developer.android.com/build)
