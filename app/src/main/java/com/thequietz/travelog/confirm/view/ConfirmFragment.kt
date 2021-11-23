package com.thequietz.travelog.confirm.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.thequietz.travelog.R
import com.thequietz.travelog.confirm.adapter.ConfirmDayAdapter
import com.thequietz.travelog.confirm.adapter.ConfirmPagerAdapter
import com.thequietz.travelog.confirm.viewmodel.ConfirmViewModel
import com.thequietz.travelog.databinding.FragmentConfirmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmFragment : Fragment() {

    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    private lateinit var _context: Context
    private lateinit var dayAdapter: ConfirmDayAdapter
    private lateinit var pageAdapter: ConfirmPagerAdapter

    private val navArgs: ConfirmFragmentArgs by navArgs()
    private val viewModel: ConfirmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm, container, false)
        _context = requireContext()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayAdapter = ConfirmDayAdapter(object : ConfirmDayAdapter.OnClickListener {
            override fun onClick(index: Int) {
                pageAdapter.submitList(viewModel.schedules.value?.get("Day ${index + 1}"))
                binding.vpConfirmPlace.let {
                    it.post {
                        it.setCurrentItem(0, true)
                    }
                }
            }
        })
        pageAdapter = ConfirmPagerAdapter()

        binding.rvConfirmHeader.adapter = dayAdapter
        binding.vpConfirmPlace.adapter = pageAdapter
        binding.vpConfirmPlace.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.schedules.observe(viewLifecycleOwner, {
            val keys = it.keys.toList()

            dayAdapter.submitList(keys)
            pageAdapter.submitList(it[keys[0]])
        })

        viewModel.getSchedulesByNavArgs(navArgs.schedules)
    }
}
