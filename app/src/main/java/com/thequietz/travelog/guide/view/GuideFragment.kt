package com.thequietz.travelog.guide.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.databinding.FragmentGuideBinding
import com.thequietz.travelog.guide.adapter.GuideMultiViewAdapter
import com.thequietz.travelog.guide.viewmodel.GuideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideFragment : Fragment() {
    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!
    private val guideViewModel by viewModels<GuideViewModel>()
    private val adapter by lazy { GuideMultiViewAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = guideViewModel
            rvGuide.adapter = adapter
        }

        with(guideViewModel) {
            dataList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
        setClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListener() {
        binding.etSearch.setOnTouchListener(
            View.OnTouchListener { view, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= binding.etSearch.right - binding.etSearch.compoundDrawables.get(
                            DRAWABLE_RIGHT
                        ).bounds.width()
                    ) {
                        searchAction()
                    }
                }
                false
            }
        )
        binding.etSearch.setOnKeyListener { _, key, event ->
            downKeyboard()
            if ((event.action == KeyEvent.ACTION_DOWN) && (key == KeyEvent.KEYCODE_ENTER)) {
                searchAction()
                true
            } else {
                false
            }
        }
    }

    fun searchAction() {
        if (binding.etSearch.text.toString() == "") {
            Toast.makeText(requireContext(), "검색어를 입력하세요!", Toast.LENGTH_SHORT).show()
        } else {
            val action = GuideFragmentDirections
                .actionGuideFragmentToSpecificGuideFragment(binding.etSearch.text.toString())
            findNavController().navigate(action)
        }
    }

    fun downKeyboard() {
        val mInputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}
