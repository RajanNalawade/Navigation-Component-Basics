package com.example.navigationcomponentbasics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationcomponentbasics.models.NoteRequest
import com.example.navigationcomponentbasics.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    val notesLiveData get() = noteRepository.notesLiveData
    val statusLiveData get() = noteRepository.statusLiveData

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }

    fun createNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }

    fun deleteNote(noteID: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteID)
        }
    }

    fun updateNote(noteID: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.updateNote(noteID, noteRequest)
        }
    }


}