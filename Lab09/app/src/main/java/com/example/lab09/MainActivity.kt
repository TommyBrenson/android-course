package com.example.lab09

import android.annotation.SuppressLint
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.widget.doOnTextChanged

const val BROADCAST_TIME_EVENT = "com.example.lab09.timeevent"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSlider()
        initEditText()

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                counter = intent.getIntExtra("counter", counter)
                val textCounter = findViewById<EditText>(R.id.counterValue)
                textCounter.setText(counter.toString())
            }
        }

        val filter = IntentFilter(BROADCAST_TIME_EVENT)
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    var myService: TimeService? = null
    var isBound = false
    var receiver: BroadcastReceiver? = null
    var interval: Int = 10
    var counter: Int = 0

    val myConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val intent = Intent(BROADCAST_TIME_EVENT)
            intent.putExtra("interval", interval)
            intent.putExtra("counter", counter)
            sendBroadcast(intent)

            val binder = service as TimeService.MyBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    fun buttonStartService(view: View) {
        findViewById<EditText>(R.id.counterValue).isEnabled = false
        startService(Intent(this, TimeService::class.java))
        bindService(Intent(this, TimeService::class.java), myConnection, Context.BIND_AUTO_CREATE)
    }

    fun buttonStopService(view: View) {
        findViewById<EditText>(R.id.counterValue).isEnabled = true
        unbindService(myConnection)
        stopService(Intent(this, TimeService::class.java))
    }

    fun buttonGetValue(view: View) {
        if (isBound) {
            findViewById<EditText>(R.id.counterValue).setText(myService!!.getCounter().toString())
        }
    }

    fun buttonResetCounter(view: View) {
        val intent = Intent(BROADCAST_TIME_EVENT)
        counter = 0
        intent.putExtra("counter", counter)
        sendBroadcast(intent)
    }

    fun initEditText() {
        val editText = findViewById<EditText>(R.id.counterValue)
        editText.setText(counter.toString())
        editText.doOnTextChanged { text, start, before, count ->
            counter = if (text.isNullOrEmpty()) counter else text.toString().toInt()
        }
    }

    fun initSlider() {
        val slider = findViewById<SeekBar>(R.id.intervalSlider)
        val intervalText = findViewById<TextView>(R.id.intervalValue)
        slider.progress = interval
        slider.max = interval * 10
        intervalText.text = getString(R.string.interval_text, interval)

        slider.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(seekBar: SeekBar?, i: Int, b: Boolean) {
                    val intervalText = findViewById<TextView>(R.id.intervalValue)
                    interval = if (i == 0) 1 else i
                    intervalText.text = "Интервал (сек.): $interval"
                    val intent = Intent(BROADCAST_TIME_EVENT)
                    intent.putExtra("interval", interval)
                    intent.putExtra("reset", false)
                    sendBroadcast(intent)

                    Log.i("SLIDER", "Interval changed!")
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            }
        )
    }
}