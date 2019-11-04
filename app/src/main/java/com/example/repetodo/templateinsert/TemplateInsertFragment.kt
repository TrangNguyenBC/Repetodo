package com.example.repetodo.templateinsert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.repetodo.R
import com.example.repetodo.databinding.FragmentTemplateListInsertBinding

class TemplateInsertFragment : Fragment() {

    private lateinit var binding: FragmentTemplateListInsertBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_list_insert, container, false)

        return binding.root
    }
}