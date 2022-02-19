package com.thequietz.travelog.ui.place.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thequietz.travelog.R
import com.thequietz.travelog.common.BaseFragment
import com.thequietz.travelog.databinding.FragmentPlaceRecommendBinding
import com.thequietz.travelog.ui.place.adapter.PlaceRecommendAdapter
import com.thequietz.travelog.ui.place.model.PlaceDetailModel
import com.thequietz.travelog.ui.place.viewmodel.PlaceRecommendViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceRecommendFragment : BaseFragment<FragmentPlaceRecommendBinding>(R.layout.fragment_place_recommend) {
    private lateinit var _context: Context
    private val viewModel: PlaceRecommendViewModel by viewModels()
    private val navArgs: PlaceRecommendFragmentArgs by navArgs()

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
