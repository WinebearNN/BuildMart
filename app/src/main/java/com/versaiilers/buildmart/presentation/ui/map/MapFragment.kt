package com.versaiilers.buildmart.presentation.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentMapBinding
import com.versaiilers.buildmart.presentation.ui.advertisement.SuggestionsAdapter
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.mapkit.search.SuggestType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val RESULT_NUMBER_LIMIT = 5
        private const val TAG = "MapFragment"
    }



    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!



    //    private val  BOUNDING_BOX: BoundingBox = BoundingBox(map.visibleRegion.bottomLeft, map.visibleRegion.topRight)

    private lateinit var searchManager: SearchManager
    private var searchSession: Session? = null
    private lateinit var suggestSession: SuggestSession
    private val SEARCH_OPTIONS: SuggestOptions = SuggestOptions().setSuggestTypes(
        SuggestType.GEO.value or
                SuggestType.BIZ.value or
                SuggestType.TRANSIT.value
    )

    private var queryFinal: String = ""

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val query = s.toString()
            if (query.isNotBlank() && (query != queryFinal)) {
                requestSuggest(query)
            } else {
                binding.recyclerViewSuggestions.visibility = View.GONE
                Log.d(TAG, "GONE")
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    private val suggestionsList: ArrayList<String> = ArrayList()
    private val suggestionsAdapter = SuggestionsAdapter { selectedText ->
        queryFinal = selectedText
        binding.editTextAddressInput.setText(selectedText)
        binding.recyclerViewSuggestions.visibility = View.GONE
        Log.d(TAG, "GONE")
        binding.mapView.visibility = View.VISIBLE
        val params = binding.linearLayoutAddress.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.linearLayoutAddress.layoutParams = params
        binding.recyclerViewSuggestions.visibility = View.GONE
        Log.d(TAG, "GONE")
        binding.editTextAddressInput.clearFocus()
        // Скрыть клавиатуру
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editTextAddressInput.windowToken, 0)
        searchByAddress(selectedText)
        binding.buttonConfirm.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        _binding = FragmentMapBinding.inflate(inflater, container, false)




        checkLocationPermission()




        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE)
        suggestSession = searchManager.createSuggestSession()




        binding.buttonConfirm.visibility = View.GONE

        // Установка камеры на начальную точку
        binding.mapView.map.move(
            CameraPosition(Point(55.751244, 37.618423), 12.0f, 0.0f, 0.0f) // Москва
        )

        // Поиск местоположения по адресу
        binding.buttonConfirm.setOnClickListener {
            val bundle = Bundle().apply {
                putString("address", queryFinal)
            }
            findNavController().navigate(R.id.action_map_to_createAdHouse, bundle)
        }


        // Установка RecyclerView для подсказок
        binding.recyclerViewSuggestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = suggestionsAdapter
        }

        // Обработчик для поля ввода
        binding.editTextAddressInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.mapView.visibility = View.GONE
                binding.recyclerViewSuggestions.visibility = View.VISIBLE
                Log.d(TAG, "VISIBLE addressInput.setOnFocusChangeListener")
                val params = binding.linearLayoutAddress.layoutParams
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.linearLayoutAddress.layoutParams = params
            }
        }

//        addressInput.setOnClickListener {
//            mapView.visibility=View.GONE
//            suggestionsRecyclerView.visibility=View.VISIBLE
//            val params = linearLayout.layoutParams
//            params.height = ViewGroup.LayoutParams.MATCH_PARENT
//            linearLayout.layoutParams = params
//        }


        // Добавление слушателя текста в поле ввода
        binding.editTextAddressInput.addTextChangedListener(textWatcher)

        return binding.root
    }

    private fun requestSuggest(query: String) {
        val visibleRegion = binding.mapView.map.visibleRegion // Получение текущей видимой области
        val boundingBox =
            BoundingBox(visibleRegion.bottomLeft, visibleRegion.topRight) // Создание BoundingBox

//        // Скрытие списка подсказок перед запросом
//        suggestionsRecyclerView.visibility = View.GONE

        // Выполнение запроса подсказок
        suggestSession.suggest(
            query,
            boundingBox,
            SEARCH_OPTIONS,
            object : SuggestSession.SuggestListener {


                override fun onResponse(p0: SuggestResponse) {
                    suggestionsList.clear()
                    for (i in 0 until RESULT_NUMBER_LIMIT.coerceAtMost(p0.items.size)) {
                        suggestionsList.add(p0.items[i].displayText!!)
                    }
                    suggestionsAdapter.updateSuggestions(suggestionsList)
                    binding.recyclerViewSuggestions.visibility = View.VISIBLE
                    Log.d(TAG, "VISIBLE requestSuggest onResponse")

                }

                override fun onError(error: com.yandex.runtime.Error) {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка получения подсказок: ${error.javaClass.simpleName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun searchByAddress(query: String) {
        // Очистка предыдущей сессии поиска
        searchSession?.cancel()

        binding.recyclerViewSuggestions.visibility = View.GONE
        Log.d(TAG, "GONE")


        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(binding.mapView.map.visibleRegion),
            SearchOptions(),
            object : Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    if (response.collection.children.isNotEmpty()) {
                        val resultLocation =
                            response.collection.children.first().obj!!.geometry.first().point
                        resultLocation?.let {
                            binding.mapView.map.mapObjects.clear()
                            binding.mapView.map.mapObjects.addPlacemark(it)
                            binding.mapView.map.move(
                                CameraPosition(it, 15.0f, 0.0f, 0.0f)
                            )
                        }
                    }
                }

                override fun onSearchError(error: com.yandex.runtime.Error) {
                    // Обработка ошибки поиска
                    Toast.makeText(
                        requireContext(),
                        "Ошибка поиска: ${error.javaClass.simpleName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
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


    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Очищаем все слушатели
        binding.editTextAddressInput.onFocusChangeListener=null

        binding.editTextAddressInput.removeTextChangedListener(textWatcher)

        // Отменяем текущую сессию поиска
        searchSession?.let {
            it.cancel()
            searchSession = null
        }
        binding.mapView.removeAllViews()

        // Очищаем адаптер и список подсказок
        suggestionsList.clear()

        // Сбрасываем ссылку на RecyclerView (если требуется)
        binding.recyclerViewSuggestions.adapter = null

        // Отмените сессии поиска и подсказок
        searchSession?.cancel()
        binding.recyclerViewSuggestions.adapter = null
        binding.linearLayoutAddress.removeAllViews()  // Удаляет все дочерние элементы из LinearLayout
        binding.linearLayoutAddress.onFocusChangeListener = null  // Удаляет слушателя фокуса, если он был
        // Освобождаем MapView
        binding.buttonConfirm.setOnClickListener(null)

        _binding = null

    }


}


//    private fun requestSuggestions(query: String) {
//        suggestSession.suggest(
//            query,
//            VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
//            SuggestOptions(),
//            object : SuggestSession.SuggestListener {
//                override fun onResponse(suggestions: MutableList<SuggestItem>) {
//                    if (suggestions.isNotEmpty()) {
//                        // Обновляем адаптер для отображения подсказок
//                        suggestionsAdapter.updateSuggestions(suggestions.map { it.displayText })
//                        binding.suggestionsRecyclerView.visibility = View.VISIBLE
//                    } else {
//                        binding.suggestionsRecyclerView.visibility = View.GONE
//                    }
//                }
//
//                override fun onError(error: com.yandex.runtime.Error) {
//                    Toast.makeText(requireContext(), "Ошибка подсказок: ${error.javaClass.simpleName}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//    }


//// Добавление плейсмарка на карту
//val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.ic_hz)
//val placemark = mapView.map.mapObjects.addPlacemark(Point(55.751225, 37.62954), imageProvider)
////        placemark.addTapListener(placemarkTapListener)
//
//// Обработка кнопки "Подтвердить"
//btnConfirm.setOnClickListener {
//    val address = etAddress.text.toString()
//
//    if (address.isNotBlank()) {
//        Toast.makeText(requireContext(), "Адрес: $address", Toast.LENGTH_SHORT).show()
//    } else {
//        Toast.makeText(requireContext(), "Введите адрес", Toast.LENGTH_SHORT).show()
//    }
//}

//    private val placemarkTapListener = MapObjectTapListener { _, point ->
//        Toast.makeText(
//            requireContext(),
//            "Tapped the point (${point.longitude}, ${point.latitude})",
//            Toast.LENGTH_SHORT
//        ).show()
//        true
//    }