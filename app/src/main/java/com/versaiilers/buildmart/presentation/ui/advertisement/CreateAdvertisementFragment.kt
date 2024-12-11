package com.versaiilers.buildmart.presentation.ui.advertisement

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentCreateAdvertisementBinding
import com.versaiilers.buildmart.databinding.FragmentMainAuthBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class CreateAdvertisementFragment : Fragment() {

    private var _binding: FragmentCreateAdvertisementBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var etAddress: EditText
    private lateinit var btnConfirm: Button

    private val placemarkTapListener = MapObjectTapListener { _, point ->
        Toast.makeText(
            requireContext(),
            "Tapped the point (${point.longitude}, ${point.latitude})",
            Toast.LENGTH_SHORT
        ).show()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализируйте Yandex MapKit с API ключом
        MapKitFactory.setApiKey("03b19b0c-c370-4e66-b588-08a3fdabbcdc")
        MapKitFactory.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAdvertisementBinding.inflate(inflater, container, false)

        checkLocationPermission()

        // Инициализация UI-компонентов
        mapView = binding.yandexMapView
        etAddress = binding.etAddress
        btnConfirm = binding.btnConfirm

        // Установите начальную позицию карты (например, центр Москвы)
        mapView.map.move(
            CameraPosition(Point(55.751244, 37.618423), 10.0f, 0.0f, 0.0f)
        )

        // Добавление плейсмарка на карту
        val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.ic_hz)
        val placemark = mapView.map.mapObjects.addPlacemark(Point(55.751225, 37.62954), imageProvider)
        placemark.addTapListener(placemarkTapListener)

        // Обработка кнопки "Подтвердить"
        btnConfirm.setOnClickListener {
            val address = etAddress.text.toString()
            if (address.isNotBlank()) {
                Toast.makeText(requireContext(), "Адрес: $address", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Введите адрес", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Запрашиваем разрешение
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Разрешение предоставлено
            } else {
                // Разрешение отклонено
                Toast.makeText(
                    requireContext(),
                    "Доступ к местоположению отклонен",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}