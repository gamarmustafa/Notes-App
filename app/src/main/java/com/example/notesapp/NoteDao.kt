package com.example.notesapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(employeeEntity: NoteEntity)

    @Update
    suspend fun update(employeeEntity: NoteEntity)

    @Query("DELETE FROM `notes-table` WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM `notes-table`")
    fun fetchAllEmployees(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM `notes-table` where id =:id")
    fun fetchEmployeeById(id: Int): Flow<NoteEntity>
}