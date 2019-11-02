package com.example.repetodo.templatelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.repetodo.R
import com.example.repetodo.databinding.FragmentTemplateListBinding

class TemplateListFragment : Fragment() {
    private lateinit var binding: FragmentTemplateListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_list, container, false)
        binding.textView.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_templateListFragment_to_templateFragment))

        return binding.root
    }
}