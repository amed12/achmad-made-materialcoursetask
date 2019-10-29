package com.achmad.madeacademy.dicodingmadeclass

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.achmad.madeacademy.dicodingmadeclass.DataManagerService.Companion.CANCEL_MESSAGE
import com.achmad.madeacademy.dicodingmadeclass.DataManagerService.Companion.FAILED_MESSAGE
import com.achmad.madeacademy.dicodingmadeclass.DataManagerService.Companion.PREPARATION_MESSAGE
import com.achmad.madeacademy.dicodingmadeclass.DataManagerService.Companion.SUCCESS_MESSAGE
import com.achmad.madeacademy.dicodingmadeclass.DataManagerService.Companion.UPDATE_MESSAGE
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), HandlerCallback {
    override fun onPreparation() {
        Toast.makeText(this, "Memulai memuat data", Toast.LENGTH_SHORT).show()
    }

    override fun updateProgress(progress: Long) {
        Log.d("Progress", "update: $progress")
        progress_bar.progress = progress.toInt()
    }

    override fun loadSuccess() {
        Toast.makeText(this, "Berhasil", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@MainActivity, MahasiswaActivity::class.java))
    }

    override fun loadFailed() {
        Toast.makeText(this, "Gagal", Toast.LENGTH_LONG).show()
    }

    override fun loadCancel() {
        finish()
    }

    private lateinit var mBoundService: Messenger
    private var mServiceBound: Boolean = false
    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBoundService = Messenger(service)
            mServiceBound = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mBoundServiceIntent = Intent(this@MainActivity, DataManagerService::class.java)
        val mActivityMessenger = Messenger(IncomingHandler(this))
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER, mActivityMessenger)
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

    private class IncomingHandler(callback: HandlerCallback) : Handler() {
        private var weakCallBack: WeakReference<HandlerCallback> = WeakReference(callback)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PREPARATION_MESSAGE -> weakCallBack.get()?.onPreparation()
                UPDATE_MESSAGE -> {
                    val bundle = msg.data
                    val progress = bundle.getLong("KEY_PROGRESS")
                    weakCallBack.get()?.updateProgress(progress)
                }
                SUCCESS_MESSAGE -> weakCallBack.get()?.loadSuccess()
                FAILED_MESSAGE -> weakCallBack.get()?.loadFailed()
                CANCEL_MESSAGE -> weakCallBack.get()?.loadCancel()
            }

        }
    }
}

private interface HandlerCallback {
    fun onPreparation()
    fun updateProgress(progress: Long)
    fun loadSuccess()
    fun loadFailed()
    fun loadCancel()
}
