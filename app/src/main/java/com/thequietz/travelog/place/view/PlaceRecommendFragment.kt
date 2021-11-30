package com.thequietz.travelog.place.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentPlaceRecommendBinding
import com.thequietz.travelog.place.adapter.PlaceRecommendAdapter
import com.thequietz.travelog.place.model.PlaceDetailModel
import com.thequietz.travelog.place.viewmodel.PlaceRecommendViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceRecommendFragment : Fragment() {

    private var _binding: FragmentPlaceRecommendBinding? = null
    private val binding get() = _binding!!

    private lateinit var _context: Context
    private val viewModel: PlaceRecommendViewModel by viewModels()
    private val navArgs: PlaceRecommendFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_place_recommend, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _context = requireContext()

        findNavController().apply {
            currentBackStackEntry?.savedStateHandle
                ?.getLiveData<PlaceDetailModel>("result")?.observe(
                    viewLifecycleOwner,
                    {
                        previousBackStackEntry?.savedStateHandle?.set("result", it)
                        popBackStack()
                    }
                )
        }

        setToolbar()

        viewModel.dataList.observe(viewLifecycleOwner, {
            binding.lifecycleOwner = viewLifecycleOwner
            binding.rvPlaceRecommend.adapter = PlaceRecommendAdapter(this)
            binding.rvPlaceRecommend.layoutManager = LinearLayoutManager(_context)

            (binding.rvPlaceRecommend.adapter as PlaceRecommendAdapter).submitList(it)
        })

        viewModel.loadData()
    }

    private fun setToolbar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration.Builder(navController.graph).build()

        binding.toolbar.apply {
            setupWithNavController(navController, appBarConfig)
            title = "일정 설정"
            inflateMenu(R.menu.menu_place_recommend)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_search) {
                    val action =
                        PlaceRecommendFragmentDirections.actionPlaceRecommendFragmentToPlaceSearchFragment(
                            navArgs.schedulePlaceArray
                        )
                    navController.navigate(action)
                }
                return@setOnMenuItemClickListener false
            }
        }
    }
}
