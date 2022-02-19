package com.thequietz.travelog.place.view

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.thequietz.travelog.R
import com.thequietz.travelog.common.GoogleMapFragment
import com.thequietz.travelog.databinding.FragmentPlaceSearchBinding
import com.thequietz.travelog.place.adapter.PlaceLocationAdapter
import com.thequietz.travelog.place.adapter.PlaceSearchAdapter
import com.thequietz.travelog.place.model.PlaceDetailModel
import com.thequietz.travelog.place.model.PlaceSearchModel
import com.thequietz.travelog.place.viewmodel.PlaceSearchViewModel
import com.thequietz.travelog.schedule.model.SchedulePlaceModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceSearchFragment : GoogleMapFragment<FragmentPlaceSearchBinding>(R.layout.fragment_place_search) {
    override var drawMarker = true
    override var isMarkerNumbered = true
    override var drawOrderedPolyline = false

    private lateinit var adapter: PlaceSearchAdapter
    private lateinit var locationAdapter: PlaceLocationAdapter

    private lateinit var gson: Gson
    private lateinit var _context: Context
    private lateinit var inputManager: InputMethodManager

    private val viewModel: PlaceSearchViewModel by viewModels()
    private val navArgs: PlaceSearchFragmentArgs by navArgs()
    private var mapFragment: SupportMapFragment? = null

    private val DP = (Resources.getSystem().displayMetrics.density + 0.5F).toInt()

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)

        if (navArgs.schedulePlaceArray.isEmpty()) {
            return
        }
        val initLocation = navArgs.schedulePlaceArray[0]

        baseTargetList = mutableListOf(
            LatLng(
                initLocation.mapY,
                initLocation.mapX
            )
        )

        googleMap.setOnMarkerClickListener { marker ->
            marker.id
            val index = markerList.indexOfFirst { it.id == marker.id }

            if (index == -1) return@setOnMarkerClickListener false
            val model = viewModel.place.value?.get(index)

            model?.also { it ->
                val param = gson.toJson(it)
                val action =
                    PlaceSearchFragmentDirections.actionPlaceSearchFragmentToPlaceDetailFragment(
                        param,
                        isRecommended = false,
                        isGuide = false
                    )
                findNavController().navigate(action)
            }
            true
        }

        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment = mapFragment?.also {
            val mapOptions = GoogleMapOptions().useViewLifecycleInFragment(true)
            SupportMapFragment.newInstance(mapOptions)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gson = Gson()
        _context = requireContext()
        inputManager = _context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        locationAdapter = PlaceLocationAdapter(object : PlaceLocationAdapter.OnItemClickListener {
            override fun onItemClick(model: SchedulePlaceModel) {
                viewModel.selectLocation(model)
                map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(model.mapY, model.mapX)))
            }
        })

        adapter = PlaceSearchAdapter(object : PlaceSearchAdapter.OnItemClickListener {
            override fun onItemClick(model: PlaceSearchModel, position: Int): Boolean {
                val param = gson.toJson(model)
                val action =
                    PlaceSearchFragmentDirections.actionPlaceSearchFragmentToPlaceDetailFragment(
                        param,
                        isRecommended = false,
                        isGuide = false
                    )
                view.findNavController().navigate(action)
                return true
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvPlaceLocation.adapter = locationAdapter
        binding.rvPlaceSearch.adapter = adapter
        binding.rvPlaceSearch.layoutManager = LinearLayoutManager(_context)

        viewModel.place.observe(viewLifecycleOwner, {
            val nextList = it.map { model ->
                val loc = model.geometry.location
                val lat = loc.latitude
                val lng = loc.longitude
                LatLng(lat, lng)
            }.toMutableList()

            baseTargetList = nextList
            targetList.value = nextList

            (binding.rvPlaceSearch.adapter as PlaceSearchAdapter).submitList(it)
        })

        val locations = navArgs.schedulePlaceArray.toList()
        (binding.rvPlaceLocation.adapter as PlaceLocationAdapter).submitList(locations)
        viewModel.selectLocation(locations[0])
        viewModel.initPlaceList()

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
    }

    private fun setToolbar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration.Builder(navController.graph).build()

        binding.toolbar.apply {
            setupWithNavController(navController, appBarConfig)
            inflateMenu(R.menu.menu_with_search)
            val searchView = (menu.findItem(R.id.action_search).actionView as SearchView)
            searchView.apply {
                queryHint = "목적지를 입력해보세요!"
                setIconifiedByDefault(false)
                (this.findViewById(androidx.appcompat.R.id.search_src_text) as TextView)
                    .includeFontPadding = false

                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val location = viewModel.location.value ?: return false

                        viewModel.loadPlaceList(query ?: "", location.mapY, location.mapX)
                        searchView.clearFocus()
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })

                requestFocus()
            }
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun initTargetList() {
        baseTargetList = mutableListOf()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        map.clear()
        mapFragment?.onDestroyView()
    }
}
