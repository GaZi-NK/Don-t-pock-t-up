package com.example.redma.dontpicktup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toggle.setOnCheckedChangeListener{ _, isChecked ->
            //トグルボタンOnの時の動作⇒isChackedはトルクボタンの状態を判明
            if (isChecked){
                val intent = Intent(this, AlarmService::class.java)
                startService(intent)
            //トグルボタンOffの時の動作
            }else{
                val intent = Intent(this, AlarmService::class.java)
                stopService(intent)
            }

        }
    }
}
