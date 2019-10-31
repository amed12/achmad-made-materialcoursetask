package com.achmad.madeacademy.mynotesapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.achmad.madeacademy.mynotesapp.entity.DatabaseContract.AUTHORITY
import com.achmad.madeacademy.mynotesapp.entity.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.achmad.madeacademy.mynotesapp.entity.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.achmad.madeacademy.mynotesapp.entity.NoteHelper

class MyContentProvider : ContentProvider() {

    companion object{
        private const val NOTE = 1
        private const val NOTE_ID = 2
        private val sURiMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var noteHelper: NoteHelper

        init {
            sURiMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE)
            sURiMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                NOTE_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when(NOTE_ID) {
            sURiMatcher.match(uri) -> noteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(NOTE){
            sURiMatcher.match(uri) -> noteHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        noteHelper = NoteHelper.getInstance(context as Context)
        noteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(sURiMatcher.match(uri)){
            NOTE -> noteHelper.queryAll()
            NOTE_ID -> noteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (NOTE_ID){
            sURiMatcher.match(uri) -> noteHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }
}
