# 特徴
LGのAndroidタブレットに搭載されている`Qボタン`でスクショを撮る

## 便利
全画面表示しているアプリのアニメを観ながら、ポチッとな！！

## 対応機種
- Qua tab PX
- Qua tab PZ
- au LGT32 (動作確認端末)
- とか？

## 概要
1. ユーザー補助サービス`BIND_ACCESSIBILITY_SERVICE`を登録
2. MediaProjectionでスクリーンショットを取れるようにする
3. ユーザー補助サービスに接続
4. ユーザー補助サービスで物理キー(ハードウェアキー)のイベントを拾う
5. スクショの取得と保存を`別スレッド`で実行する
6. とりあえずトーストを出す(笑)
   
いつものように正常系しか実装していないので、参考程度にお願いします…  

ちょっとハマったのは`別スレッド`のところ。  
トーストでUIが動くと、キーイベントが無効化できず、本体の`Qボタン`で設定済みアプリが動いてしまうのですよ、詳しい説明は…
どこかで見たけど、メモってなかった…  

## 雑感
まず、スクショってどうやって撮るの？から始めて、MainActivityからスクショを撮れるのを確認してからサービスを作ったので、サービスから強引(static)にImageReaderなど参照してますが、よくよく調べてみると`AccessibilityService`に`takeScreenshot`なんてメソッドがあるから、もっとスマートな実装方法があるかもしれない。  

> AccessibilityService リファレンス  
> https://developer.android.com/reference/android/accessibilityservice/AccessibilityService

## 参考サイト
> How to capture key events inside a service?  
> https://stackoverflow.com/questions/3229929/how-to-capture-key-events-inside-a-service?lq=1

> Android Tips #40 AccessibilityService でユーザー補助サービスを作ってみる  
> https://dev.classmethod.jp/smartphone/android/android-tips-40-accessibility-service/

> ANDROID 5.0 アプリからスクリーンショットを撮影する  
> https://techbooster.org/android/application/17026/

# apk
とりあえずビルドした`app-release-unsigned.apk`も置いときますね！
