## MessageAlarm_CloneCoding
Firebase message service를 이용한 메시지 알림 앱

## 사용한 요소
- FirebaseMessaging
  - Token
- FirebaseMessagingService
  - gradle
  - AndroidManifest
  - FirebaseMessagingService
  - 채널 생성
  - 
  
## Firebase clould messaging
[공식 문서](https://firebase.google.com/docs/cloud-messaging?hl=ko)

[메시지 전송 예제](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send?hl=ko)

무료로 메시지를 안정적으로 전송할 수 있는 교차 플랫폼 메시징 솔루션

## Token
```kotlin
FirebaseMessaging.getInstance().token
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    this.firebaseTextview.text = it.result
                }
            }
```
통신에 필요한 토큰을 요청할 수 있다.

## Gradle
클라우드 메시징 서비스를 이용할려면 그래들에 다음 요소를 추가해야 한다
```xml
implementation platform('com.google.firebase:firebase-bom:28.4.0')
implementation 'com.google.firebase:firebase-messaging'
implementation 'com.google.firebase:firebase-analytics-ktx'
```
- platform: 버전을 지정하지 않아도 최신으로 맞춰주는 기능

## AndroidManifest
클라우드 메시징 서비스를 이용할려면 매니페스트에 서비스 클래스를 등록해야 한다.
```xml
<application
...
		<service android:name=".MyFirebaseMessagingService"
			android:exported="false">

			<intent-filter>
				<action android:name="com.google.firebase.MESSAGING_EVENT" />
			</intent-filter>
		</service>

	</application>
```
- android:exported: 다른 앱에서 서비스를 이용할 수 있게 할지 여부

## FirebaseMessagingService
```kotlin
override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
```
- 토큰이 갱싱됐을 때 토큰 변경을 서버에 알려줌
- 수명이 긴 서비스를 운영할려면 신경써야하는 부분

```kotlin
override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        ...
    }
```
- 메시지를 받았을 때 실행할 동작을 정의하는 메소드

## 채널 생성
[공식 문서](https://developer.android.com/training/notify-user/channels?hl=ko)


```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
```
- API 26 이상부터 알림 서비스 사용 가능
- 알림 서비스를 사용하려면 채널을 생성해야한다.

## 알림 생성
```kotlin
NotificationManagerCompat.from(this)
            .notify(type.id, this.createNotification(type, title, message))
```
- 메시지를 전달받으면 메시지 내용을 알림으로 생성할 수 있다.

[공식 문서](https://developer.android.com/guide/components/intents-filters?hl=ko)

[인텐트 플래그](https://developer.android.com/guide/components/activities/tasks-and-back-stack?hl=ko)

```kotlin
val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
```
- 알림을 실행하면 이동할 액티비티를 설정한다.

```kotlin
val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
```
- NotificationCompat.Builder를 사용해 알림을 생성할 수 있다.

```kotlin
notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(...)
```
- 긴 문자열을 사용하려면 스타일을 설정하면 된다.

```kotlin
notificationBuilder
                    .setStyle(
                        NotificationCompat.DecoratedCustomViewStyle()
                    )
                    .setCustomContentView(
                        RemoteViews(packageName, R.layout.view_custom_notification)
                            .apply {
                                setTextViewText(R.id.title, title)
                                setTextViewText(R.id.message, text)
                            }
```
- 알림 UI를 커스텀하고 싶은 경우 DecoratedCustomViewStyle 스타일을 지정하면 된다.
- setTextViewText를 사용하면 커스텀 UI에 텍스트를 입력할 수 있다.
