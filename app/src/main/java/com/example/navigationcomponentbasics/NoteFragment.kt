package com.example.navigationcomponentbasics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentbasics.databinding.FragmentNoteBinding
import com.example.navigationcomponentbasics.models.NoteRequest
import com.example.navigationcomponentbasics.models.NoteResponse
import com.example.navigationcomponentbasics.utils.Constants
import com.example.navigationcomponentbasics.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var mBinding: FragmentNoteBinding? = null
    private val binding get() = mBinding!!
    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindHandlers() {
        binding.btnDlt.setOnClickListener {
            note?.let {
                noteViewModel.deleteNote(it._id)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val description = binding.edtDescription.text.toString()
            val noteRequest = NoteRequest(title, description)
            if (note == null){
                noteViewModel.createNote(noteRequest)
            } else{
                noteViewModel.updateNote(note!!._id, noteRequest)
            }
        }
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Loading -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {}
                is NetworkResult.Success -> {}
            }
        })
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString(Constants.NOTE)
        if (jsonNote != null){
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note?.let {
                binding.edtTitle.setText(it.title)
                binding.edtDescription.setText(it.description)
            }
        } else{
            binding.txtEditNotes.text = "Add Notes"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}