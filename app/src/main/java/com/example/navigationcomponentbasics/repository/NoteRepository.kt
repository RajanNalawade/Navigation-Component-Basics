package com.example.navigationcomponentbasics.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.navigationcomponentbasics.api.NotesApi
import com.example.navigationcomponentbasics.models.NoteRequest
import com.example.navigationcomponentbasics.models.NoteResponse
import com.example.navigationcomponentbasics.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesApi: NotesApi) {

    private val mNotesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>> get() = mNotesLiveData!!

    private val mStatusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData get() = mStatusLiveData!!

    suspend fun getNotes() {
        mNotesLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.getNotes()
        if (response.isSuccessful && response.body() != null) {
            mNotesLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            mNotesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        } else {
            mNotesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        mStatusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.createNote(noteRequest)
        handleStatusResponse(response, "Note created")
    }

    suspend fun deleteNote(noteID: String) {
        mStatusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.deleteNote(noteID)
        handleStatusResponse(response, "Note deleted" )
    }

    suspend fun updateNote(noteID: String, noteRequest: NoteRequest) {
        mStatusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.updateNote(noteID, noteRequest)
        handleStatusResponse(response, "Note updated")
    }

    private fun handleStatusResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            mStatusLiveData.postValue(NetworkResult.Success(message))
        } else {
            mStatusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }
}