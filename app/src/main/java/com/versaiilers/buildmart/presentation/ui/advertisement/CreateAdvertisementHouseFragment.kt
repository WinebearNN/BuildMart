package com.versaiilers.buildmart.presentation.ui.advertisement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentCreateAdvertisementHouseBinding
import com.versaiilers.buildmart.domain.entity.advertisement.HouseAd
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.FINISH_STAGE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.FOUNDATION_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.GAS_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.HEATING_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.SEWERAGE_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.TV_TYPE
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WALL_MATERIAL
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WARM_FLOOR
import com.versaiilers.buildmart.domain.entity.advertisement.parameter.WATER_SUPPLY
import kotlin.reflect.KClass


class CreateAdvertisementHouseFragment : Fragment() {

    companion object {
        private const val TAG = "CreateAdvertisementFragment"
    }



    private var _binding: FragmentCreateAdvertisementHouseBinding? = null
    private val binding get() = _binding!!


    private var houseAd: HouseAd = HouseAd()

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


    private fun initUI() {


        // Получение ID чата из аргументов или другим способом
        val address = arguments?.getString("address") ?: ""
        binding.textViewAddress.text = address

//        squareText = binding.editTextSquare


        binding.linearLayoutFinishStageMain.setEntitySelectionSingular(
            Button::class
        ) { selectedText ->
            houseAd.finishStage = FINISH_STAGE.fromTranslation(selectedText)
                ?: throw IllegalArgumentException("Invalid finish stage translation: $selectedText")
            Log.i(TAG, "Finish stage is ${houseAd.finishStage.translation}")
        }

        binding.linearLayoutGasMain.setEntitySelectionSingular(
            Button::class
        ) { selectedText ->
            houseAd.gas = GAS_TYPE.fromTranslation(selectedText)
                ?: throw IllegalArgumentException("Invalid gas type translation: $selectedText")
            Log.i(TAG, "Gas type is ${houseAd.gas.translation}")
        }

        binding.linearLayoutWarmFloorMain.setEntitySelectionSingular(
            Button::class
        ) { selectedText ->
            houseAd.warmFloor = WARM_FLOOR.fromTranslation(selectedText)
                ?: throw IllegalArgumentException("Invalid warm floor translation: $selectedText")
            Log.i(TAG, "Warm floor type is ${houseAd.warmFloor.translation}")
        }

        binding.linearLayoutWaterSupplyMain.setEntitySelectionSingular(
            Button::class
        ) { selectedText ->
            houseAd.waterSupply = WATER_SUPPLY.fromTranslation(selectedText)
                ?: throw IllegalArgumentException("Invalid water supply translation: $selectedText")
            Log.i(TAG, "Water supply type is ${houseAd.waterSupply.translation}")

        }

        binding.linearLayoutPowerSupplyMinor.setEntitySelectionSingular(
            Button::class
        ) { selectedText ->
            houseAd.powerSupply = when (selectedText) {
                getString(R.string.exist) -> true
                getString(R.string.no) -> false
                else -> throw IllegalArgumentException("Invalid power supply value: $selectedText")
            }
            Log.i(TAG, "Power supply is ${houseAd.powerSupply}")

        }

        binding.linearLayoutCommunicationsMain.setEntitySelectionSingular(
            Button::class
        ) { selectedText ->
            houseAd.tvType = TV_TYPE.fromTranslation(selectedText)
                ?: throw IllegalArgumentException("Invalid tv type translation: $selectedText")
            Log.i(TAG, "TV type is ${houseAd.tvType.translation}")

        }

        binding.buttonChooseFoundationType.setOnClickListener {
            showSelectionDialog(
                "Выберите тип фундамента",
                FOUNDATION_TYPE.getAllTranslations()
            ) { selectedText ->
                houseAd.foundationType =
                    FOUNDATION_TYPE.fromTranslation(selectedText) ?: throw IllegalArgumentException(
                        "Invalid foundation type translation: $selectedText"
                    )
                binding.buttonChooseFoundationType.text = selectedText
                Log.i(TAG, "Foundation type is ${houseAd.foundationType.translation}")
            }
        }

        binding.buttonChooseHeatingType.setOnClickListener {
            showSelectionDialog(
                "Выберите тип отопления",
                HEATING_TYPE.getAllTranslations()
            ) { selectedText ->
                houseAd.heatingType =
                    HEATING_TYPE.fromTranslation(selectedText) ?: throw IllegalArgumentException(
                        "Invalid heating type translation: $selectedText"
                    )
                binding.buttonChooseHeatingType.text = selectedText
                Log.i(TAG, "Heating type is ${houseAd.heatingType.translation}")
            }
        }

        binding.buttonChooseSewerageType.setOnClickListener {
            showSelectionDialog(
                "Выберите тип канализации",
                SEWERAGE_TYPE.getAllTranslations()
            ) { selectedText ->
                houseAd.sewerageType =
                    SEWERAGE_TYPE.fromTranslation(selectedText) ?: throw IllegalArgumentException(
                        "Invalid sewerage type translation: $selectedText"
                    )
                binding.buttonChooseSewerageType.text = selectedText
                Log.i(TAG, "Sewerage type is ${houseAd.sewerageType.translation}")
            }
        }

        binding.buttonChooseWallMaterial.setOnClickListener {
            showSelectionDialog(
                "Выберите тип стен",
                WALL_MATERIAL.getAllTranslations()
            ) { selectedText ->
                houseAd.wallMaterial =
                    WALL_MATERIAL.fromTranslation(selectedText) ?: throw IllegalArgumentException(
                        "Invalid wall material translation $selectedText"
                    )
                binding.buttonChooseWallMaterial.text = selectedText
                Log.i(TAG, "Wall material is ${houseAd.wallMaterial.translation}")
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAdvertisementHouseBinding.inflate(inflater, container, false)


        initUI()

        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.action_createAdHouse_to_createAdLand)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }


}


//private fun <T : View> ViewGroup.setEntitySelectionPlural(
//    classInst: KClass<T>,
//    onSelectionChanged: (String, Boolean) -> Unit
//) {
//    val childrenList = mutableListOf<T>()
//
//    collectEntities(
//        classInst,
//        childrenList,
//        this
//    )
//
//    childrenList.forEach { child ->
//        if (child is CheckBox) {
//            child.setOnCheckedChangeListener { _, isChecked ->
//                onSelectionChanged(((child.parent as? LinearLayout)?.children?.find { it is TextView } as? TextView)?.text.toString(),
//                    isChecked)
//            }
//        } else if (child is Button) {
//            child.setOnClickListener {
//                if (child.isActivated) {
//                    child.isActivated = false
//                    child.setBackgroundColor(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.buttonInactive
//                        )
//                    )
//                    onSelectionChanged(child.text.toString(), false)
//                } else {
//                    child.setBackgroundColor(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.buttonActive
//                        )
//                    )
//                    child.isActivated = true
//                    onSelectionChanged(
//                        child.text.toString(),
//                        true
//                    )
//                }
//            }
//        }
//    }
//}
//

