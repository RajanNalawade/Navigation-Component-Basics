package com.example.navigationcomponentbasics.api

import com.example.navigationcomponentbasics.models.NoteRequest
import com.example.navigationcomponentbasics.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesApi {

    @GET("/note")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST("/note")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @PUT("/note/{note_id}")
    suspend fun updateNote(
        @Path("note_id") noteID: String,
        @Body noteRequest: NoteRequest
    ): Response<NoteResponse>

    @DELETE("/note/{note_id}")
    suspend fun deleteNote(@Path("note_id") noteID: String): Response<NoteResponse>
}