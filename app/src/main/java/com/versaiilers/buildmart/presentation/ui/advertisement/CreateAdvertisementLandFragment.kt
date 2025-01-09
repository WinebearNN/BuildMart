package com.versaiilers.buildmart.presentation.ui.advertisement

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentCreateAdvertisementLandBinding
import com.versaiilers.buildmart.domain.entity.advertisement.LandHouseAd
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.ADDITIONAL_BUILDINGS
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.LAND_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.PARKING_SPACE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.SETTLEMENT_TERRITORY
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.TRANSPORT_ACCESSIBILITY
import kotlin.reflect.KClass


class CreateAdvertisementLandFragment : Fragment() {

    companion object {
        private const val TAG = "CreateAdvertisementLandFragment"
    }


    private var _binding: FragmentCreateAdvertisementLandBinding? = null
    private val binding get() = _binding!!

    private var _landAd: LandHouseAd? = null
    private val landAd get() = _landAd!!


    private fun showSelectionDialog(
        title: String,
        dataList: Array<String>,
        onSelectionChanged: (String) -> Unit
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(dataList) { _, which ->
                onSelectionChanged(dataList[which])
            }
            .setNegativeButton("Отмена", null)
            .show()
    }


    private fun <T : View> ViewGroup.setEntitySelectionPlural(
        classInst: KClass<T>,
        onSelectionChanged: (String, Boolean) -> Unit
    ) {
        val childrenList = mutableListOf<T>()

        collectEntities(
            classInst,
            childrenList,
            this
        )

        childrenList.forEach { child ->
            if (child is CheckBox) {
                child.setOnCheckedChangeListener { _, isChecked ->
                    onSelectionChanged(((child.parent as? LinearLayout)?.children?.find { it is TextView } as? TextView)?.text.toString(),
                        isChecked)
                }
            } else if (child is Button) {
                child.setOnClickListener {
                    if (child.isActivated) {
                        child.isActivated = false
                        child.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.buttonInactive
                            )
                        )
                        onSelectionChanged(child.text.toString(), false)
                    } else {
                        child.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.buttonActive
                            )
                        )
                        child.isActivated = true
                        onSelectionChanged(
                            child.text.toString(),
                            true
                        )
                    }
                }
            }
        }
    }

    private fun <T : View> collectEntities(
        classInst: KClass<T>,
        entities: MutableList<T>,
        viewGroup: ViewGroup,
    ) {
        viewGroup.children.forEach { child ->
            if (classInst.isInstance(child)) {
                @Suppress("UNCHECKED_CAST")
                entities.add(child as T)
            } else if (child is ViewGroup) {
                collectEntities(
                    classInst,
                    entities,
                    child
                )
            }
        }
    }


    private fun <T : View> ViewGroup.setEntitySelectionSingular(
        classInst: KClass<T>,
        onSelectionChanged: (String) -> Unit
    ) {
        val entities = mutableListOf<T>()

        collectEntities(
            classInst,
            entities,
            this
        )

        entities.forEach { entity ->
            if (entity is CheckBox) {
                entity.setOnCheckedChangeListener { _, isChecked ->
                    entities.forEach { sibling ->
                        (sibling as CheckBox).isChecked = false
                    }
                    entity.isChecked = isChecked
                    onSelectionChanged(((entity.parent as? LinearLayout)?.children?.find { it is TextView } as? TextView)?.text.toString())
                }
            } else if (entity is Button) {
                entity.setOnClickListener {
                    entities.forEach { sibling ->
                        sibling.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.buttonInactive)
                        )
                    }
                    entity.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonActive))
                    onSelectionChanged(entity.text.toString())
                }
            }
        }
    }

    private fun initUI() {

        binding.editTextLandSquare.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val input = binding.editTextLandSquare.text.toString()
                landAd.landSquare = input.toDouble()
                Log.i(TAG, "Land square is $input")
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    binding.editTextLandSquare.windowToken,
                    0
                )
                binding.editTextLandSquare.clearFocus()
                true
            } else {
                false
            }
        }

        binding.linearLayoutAddBuildingsMain.setEntitySelectionPlural(
            classInst = Button::class,
        ) { selectedText, selectedFlag ->
            if (selectedFlag) {
                landAd.additionalBuildings.add(
                    ADDITIONAL_BUILDINGS.fromTranslation(selectedText)
                        ?: throw IllegalArgumentException("Invalid additional building translation $selectedText")
                )
                Log.i(TAG, "Additional buildings are: ${landAd.additionalBuildings}")
            } else {
                landAd.additionalBuildings.remove(
                    ADDITIONAL_BUILDINGS.fromTranslation(selectedText)
                        ?: throw IllegalArgumentException("Invalid additional building translation")
                )
                Log.i(TAG, "Additional buildings are: ${landAd.additionalBuildings}")
            }
        }


        binding.buttonChooseLandType.setOnClickListener {
            showSelectionDialog(
                title = "Выберите тип земельного участка",
                dataList = LAND_TYPE.getAllTranslations()
            ) { selectedText ->
                landAd.landType =
                    LAND_TYPE.fromTranslation(selectedText) ?: throw IllegalArgumentException(
                        "Invalid foundation type translation: $selectedText"
                    )
                binding.buttonChooseLandType.text = selectedText
                Log.i(TAG, "Foundation type is ${landAd.landType.translation}")
            }
        }

        binding.linearLayoutParkingSpaceMain.setEntitySelectionPlural(
            classInst = CheckBox::class
        ) { selectedText, selectedFlag ->
            if (selectedFlag) {
                landAd.parkingSpace.add(
                    PARKING_SPACE.fromTranslation(selectedText)
                        ?: throw IllegalArgumentException("Invalid parking space translation $selectedText")
                )
                Log.i(TAG, "Parking spaces are: ${landAd.parkingSpace}")
            } else {
                landAd.parkingSpace.remove(
                    PARKING_SPACE.fromTranslation(selectedText)
                        ?: throw IllegalArgumentException("Invalid parking space translation $selectedText")
                )
                Log.i(TAG, "Parking spaces are: ${landAd.parkingSpace}")
            }
        }

        binding.linearLayoutSettlementTerritoryMain.setEntitySelectionSingular(
            classInst = Button::class
        ) { selectedText ->
            landAd.settlementTerritory =
                SETTLEMENT_TERRITORY.fromTranslation(selectedText)
                    ?: throw IllegalArgumentException(
                        "Invalid settlement territory type: $selectedText"
                    )
            Log.i(TAG, "Settlement territory type is ${landAd.settlementTerritory.translation}")
        }

        binding.linearLayoutTransportAccessibilityMain.setEntitySelectionPlural(
            classInst = CheckBox::class
        ) { selectedText, selectedFlag ->
            if (selectedFlag) {
                landAd.transportAccessibility.add(
                    TRANSPORT_ACCESSIBILITY.fromTranslation(selectedText)
                        ?: throw IllegalArgumentException("Invalid transport accessibility option: $selectedText")
                )
                Log.i(TAG,"Transport accessibility options are: ${landAd.transportAccessibility} ")
            }else{
                landAd.transportAccessibility.remove(
                    TRANSPORT_ACCESSIBILITY.fromTranslation(selectedText)
                        ?: throw IllegalArgumentException("Invalid transport accessibility option: $selectedText")
                )
                Log.i(TAG,"Transport accessibility options are: ${landAd.transportAccessibility} ")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateAdvertisementLandBinding.inflate(inflater, container, false)

        _landAd = LandHouseAd()

        initUI()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        _landAd=null
    }
}


//    private fun showMultiSelectionDialog(
//        title: String,
//        dataList: Array<String>,
//        selectedItems: MutableSet<String>, // Для хранения выбранных элементов
//        onSelectionChanged: (Set<String>) -> Unit // Возвращает обновлённый набор выбранных значений
//    ) {
//        // Массив для отслеживания состояний выбранных элементов
//        val checkedItems =
//            BooleanArray(dataList.size) { index -> selectedItems.contains(dataList[index]) }
//
//        AlertDialog.Builder(requireContext())
//            .setTitle(title)
//            .setMultiChoiceItems(dataList, checkedItems) { _, which, isChecked ->
//                // Обновляем состояние выбранного элемента
//                if (isChecked) {
//                    selectedItems.add(dataList[which])
//                } else {
//                    selectedItems.remove(dataList[which])
//                }
//            }
//            .setPositiveButton("OK") { _, _ ->
//                // Передаём обновлённый список выбранных элементов
//                onSelectionChanged(selectedItems)
//            }
//            .setNegativeButton("Отмена", null)
//            .show()
//    }

//    fun collectCheckBoxes(viewGroup: ViewGroup) {
//        viewGroup.children.forEach { child ->
//            if (classInst.isInstance(child)) {
//                @Suppress("UNCHECKED_CAST")
//                childrenList.add(child as T)
//            } else if (child is ViewGroup) {
//                collectCheckBoxes(child)
//            }
//        }
//    }

