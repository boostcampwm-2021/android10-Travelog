package com.thequietz.travelog.place.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentPlaceSearchBinding
import com.thequietz.travelog.map.GoogleMapFragment
import com.thequietz.travelog.place.adapter.PlaceLocationAdapter
import com.thequietz.travelog.place.adapter.PlaceSearchAdapter
import com.thequietz.travelog.place.model.PlaceDetailModel
import com.thequietz.travelog.place.model.PlaceSearchModel
import com.thequietz.travelog.place.viewmodel.PlaceSearchViewModel
import com.thequietz.travelog.schedule.model.SchedulePlaceModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceSearchFragment : GoogleMapFragment<FragmentPlaceSearchBinding, PlaceSearchViewModel>() {

    override val viewModel: PlaceSearchViewModel by viewModels()
    override val layoutId = R.layout.fragment_place_search
    override var drawMarker = true
    override var isMarkerNumbered = true
    override var drawOrderedPolyline = false

    private lateinit var adapter: PlaceSearchAdapter
    private lateinit var locationAdapter: PlaceLocationAdapter

    private lateinit var gson: Gson
    private lateinit var _context: Context
    private lateinit var inputManager: InputMethodManager

    private val navArgs: PlaceSearchFragmentArgs by navArgs()
    private var googleMap: GoogleMap? = null

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)

        this.googleMap = googleMap

        if (navArgs.schedulePlaceArray.isEmpty()) {
            return
        }
        val initLocation = navArgs.schedulePlaceArray[0]

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    initLocation.mapY,
                    initLocation.mapX
                )
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gson = Gson()
        _context = requireContext()
        inputManager = _context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        locationAdapter = PlaceLocationAdapter(object : PlaceLocationAdapter.OnItemClickListener {
            override fun onItemClick(model: SchedulePlaceModel) {
                viewModel.selectLocation(model)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(model.mapY, model.mapX)))
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

        binding.etPlaceSearch.setOnKeyListener { v, code, event ->
            if (code != KeyEvent.KEYCODE_ENTER || event.action != KeyEvent.ACTION_DOWN) return@setOnKeyListener false

            val query = (v as EditText).text.toString()
            val location = viewModel.location.value
            if (location == null) return@setOnKeyListener false

            viewModel.loadPlaceList(query, location.mapY, location.mapX)
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            true
        }

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

        binding.etPlaceSearch.requestFocus()

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
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun initTargetList() {
        baseTargetList = mutableListOf()
    }
}
