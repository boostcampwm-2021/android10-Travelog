package com.thequietz.travelog.guide.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentGuideBinding

private const val TAG = "AREA_LIST"

class GuideFragment : Fragment() {
    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide, container, false)
        return binding.root
    }
}
