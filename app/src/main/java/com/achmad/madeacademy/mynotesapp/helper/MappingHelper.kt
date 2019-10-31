package com.achmad.madeacademy.mynotesapp.helper

import android.database.Cursor
import com.achmad.madeacademy.mynotesapp.entity.DatabaseContract
import com.achmad.madeacademy.mynotesapp.entity.Note

object MappingHelper{
    fun mapCursorToArrayList(noteCursor: Cursor): ArrayList<Note>{
        val noteList = ArrayList<Note>()
        noteCursor.moveToFirst()
        while (noteCursor.moveToNext()){
            val id = noteCursor.getInt(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
            val title = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
            val description = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
            val date = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))
            noteList.add(Note(id, title, description, date))
        }

        return noteList
    }

    fun mapCursorToObject(noteCursor: Cursor): Note{
        noteCursor.moveToNext()
        val id = noteCursor.getInt(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
        val title = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
        val description = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
        val date = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))
        return Note(id, title, description, date)
    }
}