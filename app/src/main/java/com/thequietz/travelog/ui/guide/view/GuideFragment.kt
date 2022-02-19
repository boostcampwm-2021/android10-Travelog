package com.thequietz.travelog.ui.guide.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thequietz.travelog.R
import com.thequietz.travelog.common.BaseFragment
import com.thequietz.travelog.databinding.FragmentGuideBinding
import com.thequietz.travelog.ui.guide.adapter.GuideMultiViewAdapter
import com.thequietz.travelog.ui.guide.viewmodel.GuideViewModel
import com.thequietz.travelog.util.makeSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideFragment : BaseFragment<FragmentGuideBinding>(R.layout.fragment_guide) {
    private val guideViewModel by viewModels<GuideViewModel>()
    private val adapter by lazy { GuideMultiViewAdapter() }

    private lateinit var _context: Context
    private lateinit var inputManager: InputMethodManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _context = requireContext()
        inputManager = _context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        with(binding) {
            viewModel = guideViewModel
            rvGuide.adapter = adapter
        }

        with(guideViewModel) {
            dataList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }

        findNavController().apply {
            currentBackStackEntry?.savedStateHandle
                ?.also {
                    it.getLiveData<Boolean>("toSchedulePlace").observe(
                        viewLifecycleOwner,
                        { isClicked ->
                            if (isClicked) {
                                Log.d("TAG", "Move to Schedule")
                                it.remove<Boolean>("toSchedulePlace")
                                requireActivity()
                                    .findViewById<BottomNavigationView>(R.id.bottom_navigation)
                                    .selectedItemId = R.id.schedule
                            }
                        }
                    )
                }
        }

        setClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListener() {
        binding.etSearch.setOnTouchListener { view, event ->
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

        binding.etSearch.setOnKeyListener { _, key, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (key == KeyEvent.KEYCODE_ENTER)) {
                inputManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                searchAction()
                closeKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun searchAction() {
        if (binding.etSearch.text.toString().trim() == "") {
            makeSnackBar(binding.clGuide, "검색어를 입력하세요!")
        } else {
            val action = GuideFragmentDirections
                .actionGuideFragmentToSpecificGuideFragment(binding.etSearch.text.toString(), "Search")
            findNavController().navigate(action)
        }
    }

    private fun closeKeyboard() {
        val mInputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}
