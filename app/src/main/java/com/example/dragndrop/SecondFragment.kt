package com.example.dragndrop

import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dragndrop.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dropTarget.prepareAsDropTarget(requireActivity(), object : DragEvents() {
            override fun onClipDataDropped(event: DragEvent) {
                onDrop(event)
            }
        })
    }

    private fun onDrop(event: DragEvent) {
        event.toClipDataContents().forEach { clipDataContent ->
            when (clipDataContent) {
                is ClipDataContent.ImageClipData -> {
                    binding.droppedTextContent.visibility = View.GONE
                    binding.droppedImageContent.visibility = View.VISIBLE
                    val uri = clipDataContent.imageData
                    binding.droppedImageContent.setImageURI(uri)
                }
                is ClipDataContent.FileClipData -> {
                    binding.droppedTextContent.text = clipDataContent.fileData.toString()
                    binding.droppedTextContent.visibility = View.VISIBLE
                    binding.droppedImageContent.visibility = View.GONE
                }
                is ClipDataContent.TextClipData -> {
                    binding.droppedTextContent.text = clipDataContent.data
                    binding.droppedTextContent.visibility = View.VISIBLE
                    binding.droppedImageContent.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}