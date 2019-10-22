package com.achmad.madeacademy.dicodingmadeclass

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(),MyAsyncCallback{

    companion object{
        private const val INPUT_STRING = "Halo ini Demo AsyncTask!!"
    }
    override fun onPreExecute() {
        tv_status.setText(R.string.status_pre)
        tv_desc.text = INPUT_STRING

    }

    override fun onPostExecute(result: kotlin.String) {
        tv_status.setText(R.string.status_post)
        tv_desc.text = result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val demoAsync = DemoAsync(this)
        demoAsync.execute(INPUT_STRING)
    }


    private class DemoAsync(myListener: MyAsyncCallback) : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Log.d(LOG_ASYNC,"Status : onPreExecute")
            val myListener = myListener.get()
            myListener?.onPreExecute()
        }

        override fun doInBackground(vararg params: kotlin.String?): kotlin.String {
            Log.d(LOG_ASYNC,"status : doInBackground")
            var output :kotlin.String? = null
            try {
                val input = params[0]
                output = "$input selamat belajar!!"
                Thread.sleep(2000)
            }catch (e : Exception){
                Log.d(LOG_ASYNC,e.message)
            }
            return output.toString()
        }

        override fun onPostExecute(result: kotlin.String) {
            super.onPostExecute(result)
            Log.d(LOG_ASYNC,"Status : onPostExecute")
            val myListener = this.myListener.get()
            myListener?.onPostExecute(result)
        }

        companion object {
            private val LOG_ASYNC = "DemoAsync"
        }

        private val myListener: WeakReference<MyAsyncCallback> = WeakReference(myListener)

    }

}

internal interface MyAsyncCallback {
    fun onPreExecute()
    fun onPostExecute(result : kotlin.String)

}