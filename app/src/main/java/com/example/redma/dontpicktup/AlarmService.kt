package com.example.redma.dontpicktup

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.IBinder

class AlarmService : Service(),SensorEventListener {
    //スマホが持っていかれたと判断するための値
    private val threshold: Float = 15f
    private var mp: MediaPlayer? = null //  音声ファイルを再生するメディアプレイヤー
    private val oValue: Array<Float> = arrayOf(0f, 0f, 0f)  //Floatの配列


    //サービスが開始されたタイミングで呼び出し
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //センサーマネージャーインスタンスを取得⇒王ジェクト型を返すのでキャスト
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //↑から加速度センサーのオブジェクトを取得
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        /*リスナーを登録⇒これをしないとAndroidシステムがアプリにセンサーの値を返してくれない
          ⇒引数(このアプリに(リスナーオブジェクト)、加速度センサーを(センサーオブジェクト)、この方法で返す(データ取得のレート))*/
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)  //リスナーの登録を解除
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return   //センサーがnullでないことをチェック
        //加速度センサーであれば実行
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            //Math.absは絶対値を出す
            val speed = Math.abs(event.values[0] - oValue[0]) +
                    Math.abs(event.values[1] - oValue[1])+
                    Math.abs(event.values[2] - oValue[2])
            //15f以上傾いていれば音声を再生
            if (speed > threshold) {
                mp = MediaPlayer.create(applicationContext,R.raw.voice)
                mp?.start()
            }
            //この処理の意味⇒スマホをサッと取られた時に音声を流すため前の値と15f以上が差異があればという意味です
            oValue[0] = event.values[0]
            oValue[1] = event.values[1]
            oValue[2] = event.values[2]
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
