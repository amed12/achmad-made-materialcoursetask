package com.achmad.madeacademy.dicodingmadeclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.File
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var path : File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_new.setOnClickListener(this)
        button_open.setOnClickListener(this)
        button_save.setOnClickListener(this)

    }
    override fun onClick(view: View) {
        when(view.id){
           button_new.id -> newFile()
           button_open.id -> showList()
           button_save.id -> saveFile()
        }
    }

    private fun newFile(){
        edit_file.setText("")
        edit_title.setText("")
        Toast.makeText(this,"Clearing  file", Toast.LENGTH_SHORT).show()
    }

    private fun showList(){
        val arrayList = ArrayList<String>()
        val path: File = filesDir
        Collections.addAll(arrayList, *path.list() as Array<String>)
        val items = arrayList.toTypedArray<CharSequence>()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih file yang diinginkan")
        builder.setItems(items) { dialog, item -> loadData(items[item].toString()) }
        val alert  =builder.create()
        alert.show()
    }

    private fun loadData(title: String){
        val fileModel = FileHelper.readFromFile(this,title)
        edit_title.setText(fileModel.filename)
        edit_file.setText(fileModel.data)
        Toast.makeText(this,"Loading "+fileModel.filename+" data",Toast.LENGTH_SHORT).show()
    }

    private fun saveFile(){
        when{
            edit_title.text.toString().isEmpty() -> Toast.makeText(this,"Title harus diisi lebih dahulu",Toast.LENGTH_SHORT).show()
            edit_file.text.toString().isEmpty() -> Toast.makeText(this, "Kontenct harus diisi lebih dahulu",Toast.LENGTH_SHORT).show()
            else -> {
                val title = edit_title.text.toString()
                val text = edit_file.text.toString()
                val fileModel = FileModel()
                fileModel.filename = title
                fileModel.data = text
                FileHelper.writeToFile(fileModel, this)
                Toast.makeText(this, "Saving ..." + fileModel.filename+" File",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
