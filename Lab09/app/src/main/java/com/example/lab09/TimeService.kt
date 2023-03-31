package com.example.lab09

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimeService : Service() {
    private val myBinder = MyBinder()
    private var receiver: BroadcastReceiver? = null

    private lateinit var job: Job
    private var counter = 0
    private var interval = 1

    override fun onCreate() {
        super.onCreate()
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                interval = intent.getIntExtra("interval", interval)
                counter = intent.getIntExtra("counter", counter)
            }
        }

        val filter = IntentFilter(BROADCAST_TIME_EVENT)
        registerReceiver(receiver, filter)
    }

    inner class MyBinder: Binder() {
        fun getService() : TimeService {
            return this@TimeService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        interval = intent!!.getIntExtra("interval", interval)

        job = GlobalScope.launch {
            while (true) {
                delay(interval * 1000L)
                Log.d("SERVICE", "Timer Is Ticking: $counter")
                Log.d("SERVICE", "Timer Interval: $interval")
                counter++
                val intent = Intent(BROADCAST_TIME_EVENT)
                intent.putExtra("counter", counter)
                sendBroadcast(intent)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("SERVICE", "onDestroy")
        job.cancel()
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    fun getCounter(): Int {
        return counter
    }
}