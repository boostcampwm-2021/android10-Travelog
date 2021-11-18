package com.thequietz.travelog.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import com.thequietz.travelog.R

abstract class GoogleMapFragment<B : ViewDataBinding, VM : ViewModel> :
    Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    protected lateinit var binding: B
    abstract val viewModel: VM

    abstract val layoutId: Int

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val locationPermission = Manifest.permission.ACCESS_COARSE_LOCATION

    protected lateinit var map: GoogleMap
    private lateinit var mapViewBound: LatLngBounds
    protected var zoomLevel: Float = 11f

    var targetList: MutableLiveData<MutableList<LatLng>> = MutableLiveData(mutableListOf())
    private var targetCount: Int = 0

    protected var markerList: MutableList<Marker> = mutableListOf()
    private var polylineList: MutableList<Polyline> = mutableListOf()

    protected var isInitial: Boolean = true

    abstract fun initViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted)
                    setUserLocationAction()
                else
                    showPermissionDeniedSnackBar(binding.root)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        (childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment)
            .getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        initViewModel()

        initTargetList()
        initMapViewBound()
    }

    open fun initTargetList() {
        targetList.value = mutableListOf()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setMinZoomPreference(6f)
            setOnMapLoadedCallback {
                targetList.observe(viewLifecycleOwner, {
                    initMapViewBound()
                    map.moveCamera(
                        if (targetCount > 1)
                            CameraUpdateFactory.newLatLngBounds(
                                mapViewBound, 100
                            )
                        else
                            CameraUpdateFactory.newLatLngZoom(
                                mapViewBound.center, zoomLevel
                            )
                    )
                })
            }
            uiSettings.apply {
                isZoomControlsEnabled = true
            }
        }
        setUserLocationAction()
        addMapComponents()
        isInitial = false
    }

    open fun addMapComponents() {}

    private fun setUserLocationAction() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                locationPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(locationPermission)
        } else {
            map.uiSettings.isMyLocationButtonEnabled = true
            map.isMyLocationEnabled = true
        }
    }

    private fun showPermissionDeniedSnackBar(layout: View) {
        Snackbar.make(layout, "현재 위치 기능 사용 불가", Snackbar.LENGTH_INDEFINITE).apply {
            setAction("권한 설정") { setUserLocationAction() }
        }.show()
    }

    private fun initMapViewBound() {
        targetList.value.let { list ->
            if (list != null) {
                targetCount = list.size

                mapViewBound =
                    if (targetCount > 0)
                        LatLngBounds(
                            LatLng(list.minOf { it.latitude }, list.minOf { it.longitude }),
                            LatLng(list.maxOf { it.latitude }, list.maxOf { it.longitude })
                        )
                    else
                        LatLngBounds(LatLng(37.55, 126.99), LatLng(37.55, 126.99))
            }
        }

        Log.d("initMap", mapViewBound.center.toString())
    }

    fun createMarker(
        vararg markerPos: LatLng,
        colorId: Int = R.color.blue_travelog,
        isNumbered: Boolean = false
    ) {
        markerPos.forEach { position ->
            val markerView = if (isNumbered)
                createMarkerView(colorId, markerList.size + 1)
            else createMarkerView(colorId)
            val markerOption = MarkerOptions().position(position)
                .icon((markerView)?.let { BitmapDescriptorFactory.fromBitmap(it) })
            map.addMarker(markerOption)?.let { markerList.add(it) }
        }
    }

    fun deleteMarker(pos: Int) {
        if (pos < markerList.size) {
            markerList[pos].remove()
            markerList.removeAt(pos)
        }
    }

    private fun createMarkerView(colorId: Int, number: Int = -1): Bitmap? {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_map_marker, null)
            ?.apply {
                setTint(ContextCompat.getColor(requireContext(), colorId))
            } ?: return null

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        if (number == -1)
            return bitmap

        val paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = 48f
        paint.textAlign = Paint.Align.CENTER

        canvas.drawText(number.toString(), canvas.width / 2f, canvas.height / 2f, paint)

        return bitmap
    }

    fun createPolyline(
        fromMarker: Marker,
        toMarker: Marker,
        colorId: Int = R.color.green_travelog
    ) {
        val polylineOptions = PolylineOptions()
            .addAll(mutableListOf(fromMarker.position, toMarker.position))
            .color(colorId)
            .width(5f)

        polylineList.add(
            map.addPolyline(polylineOptions).apply {
                this.pattern = listOf(Dash(20f), Gap(20f))
            }
        )
    }

    fun deletePolyline(pos: Int) {
        if (pos < polylineList.size) {
            polylineList[pos].remove()
            polylineList.removeAt(pos)
        }
    }

    fun changeMarkerOrder(
        fromPosition: Int,
        toPosition: Int,
        drawOrderedPolyline: Boolean = false
    ) {
        if (fromPosition >= markerList.size && toPosition >= markerList.size)
            return

        val temp = mutableListOf<Marker>().apply {
            addAll(
                markerList.apply {
                    val tempPos = markerList[fromPosition]
                    markerList.removeAt(fromPosition)
                    markerList.add(toPosition, tempPos)
                }
            )
        }

        markerList.forEach { it.remove() }
        markerList.clear()
        polylineList.forEach { it.remove() }
        polylineList.clear()

        createMarker(*temp.map { it.position }.toTypedArray())

        if (drawOrderedPolyline) {
            markerList.forEachIndexed { index, marker ->
                if (index < markerList.size - 1) {
                    createPolyline(marker, markerList[index + 1])
                }
            }
        }
    }

    // Set Marker Listener
    override fun onMarkerClick(p0: Marker) = true
}
