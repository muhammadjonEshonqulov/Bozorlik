package uz.bozorliq.ui.main

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import dagger.hilt.android.AndroidEntryPoint
import uz.bozorliq.R
import uz.bozorliq.base.AddCategoryDialog
import uz.bozorliq.base.BaseFragment
import uz.bozorliq.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate), OnClickListener {

    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        binding.menu.setOnClickListener(this)
        binding.addCategory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.addCategory -> {
                val dialog = AddCategoryDialog(binding.root.context, getString(R.string.add_category))
                dialog.setOnCancelClick {
                    dialog.dismiss()
                }
                dialog.setOnSubmitClick { type, text ->
                    dialog.dismiss()
                    when(type){
                        1 -> {

                        }
                        2 -> {

                        }
                    }
                }
                dialog.show()
            }
            binding.menu -> {

            }
        }
    }
}