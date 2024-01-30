package com.disc.notesapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDAO {

    @Insert
    suspend fun insertNotes(notes: Notes)

    @Delete
    suspend fun deleteNotes(notes: Notes)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Notes>>

    @Query("UPDATE notes SET title = :updatedTitle, description = :updatedDescription WHERE title = :originalTitle")
    suspend fun updateNotes(updatedTitle: String, updatedDescription: String, originalTitle: String)
}