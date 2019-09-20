package com.example.repetodo.mainlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.repetodo.R
import com.example.repetodo.databinding.FragmentMainListBinding

class MainListFragment : Fragment() {
    private lateinit var binding: FragmentMainListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        return binding.root
    }

}