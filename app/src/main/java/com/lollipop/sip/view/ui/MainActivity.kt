package com.lollipop.sip.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.lollipop.sip.R
import com.lollipop.sip.databinding.ActivityMainBinding
import com.lollipop.sip.service.model.BangunanResult
import com.lollipop.sip.util.Constant
import com.lollipop.sip.util.ResultOfNetwork
import com.lollipop.sip.viewModel.DataStoreViewModel
import com.lollipop.sip.viewModel.MainViewModel
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import timber.log.Timber
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private lateinit var _binding: ActivityMainBinding

    private lateinit var _viewModelDataStore: DataStoreViewModel
    private lateinit var _viewModel: MainViewModel

    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private var mapboxMap: MapboxMap? = null

    private var locationEngine: LocationEngine? = null
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5

    // Variables needed to listen to location updates
    private val callback = MainActivityLocationCallback(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()

        with(_binding) {
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@MainActivity)
            imgWarning.setImageResource(R.drawable.ic_warn)
            tvWarning.text =
                "Anda Belum Menambahkan Data Penghuni, Silakan Klik Tambah Data Penghuni"
        }

        observeDataStore()

    }

    private fun initializeViewModel() {
        _viewModelDataStore = ViewModelProvider(this).get(DataStoreViewModel::class.java)
        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun observeDataStore() {
        _viewModelDataStore.loginStatus.observe(this, {
            if (!it) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                _viewModelDataStore.userData.observe(this, {
                    _binding.user.text = it[1]
                    _binding.txtAlamat.text = "${it[2]} - ${it[3]}"
                    _viewModel.showBangunan("show_bangunan", it[0])
                })
            }
        })

        _viewModel.bangunan.observe(this, {
            when (it) {
                is ResultOfNetwork.Success -> {
                    isSuccessNetworkCallback(
                        it.value.code,
                        it.value.data
                    )
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun isSuccessNetworkCallback(code: Int?, data: List<BangunanResult>?) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Timber.d("Request not found")
            }
            Constant.Network.REQUEST_SUCCESS -> {
                if (data != null) {
                    data[0].id_bangunan?.let {
                        data[0].lat?.let { it1 ->
                            data[0].lng?.let { it2 ->
                                initDropMarker(
                                    it,
                                    it1, it2
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        mapboxMap.setStyle(
            Style.MAPBOX_STREETS
        ) { loadedMapStyle: Style? ->
            enableLocationComponent(
                loadedMapStyle!!
            )
        }

        mapboxMap.onInfoWindowClickListener = MapboxMap.OnInfoWindowClickListener {
            Toast.makeText(this@MainActivity, "Test click window", Toast.LENGTH_LONG).show()
            true
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG)
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap?.style?.let { enableLocationComponent(it) }
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG)
                .show()
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.hijau_muda))
                .build()

            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            mapboxMap?.locationComponent?.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.NORMAL
            }


        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun initDropMarker(idBg: String, lat: Double, lng: Double) {
        mapboxMap?.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title("Lihat Detail Bangunan")
        )
        val position = CameraPosition.Builder()
            .target(LatLng(lat, lng))
            .zoom(14.0)
            .tilt(20.0)
            .build()
        mapboxMap?.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000)

        mapboxMap?.onInfoWindowClickListener = MapboxMap.OnInfoWindowClickListener {
            startActivity(
                Intent(this, BangunanActivity::class.java)
                    .putExtra("id_bangunan", idBg)
            )
            return@OnInfoWindowClickListener true
        }
    }

    private class MainActivityLocationCallback(activity: MainActivity) :
        LocationEngineCallback<LocationEngineResult> {
        private val activityWeakReference: WeakReference<MainActivity>

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        override fun onSuccess(result: LocationEngineResult) {
            val activity = activityWeakReference.get()
            if (activity != null) {
                val location = result.lastLocation ?: return

                // Create a Toast which displays the new location's coordinates
//                Toast.makeText(activity, String.format(activity.getString(R.string.new_location),
//                        String.valueOf(result.getLastLocation().getLatitude()), String.valueOf(result.getLastLocation().getLongitude())),
//                        Toast.LENGTH_SHORT).show();

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.lastLocation != null) {
                    activity.mapboxMap!!.locationComponent.forceLocationUpdate(result.lastLocation)
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        override fun onFailure(exception: Exception) {
            Log.d("LocationChangeActivity", exception.localizedMessage)
            val activity = activityWeakReference.get()
            if (activity != null) {
                Toast.makeText(
                    activity, exception.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        init {
            activityWeakReference = WeakReference(activity)
        }
    }

    override fun onStart() {
        super.onStart()
        _binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        _binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        _binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        _binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _binding.mapView.onLowMemory()
    }
}