package uz.bozorliq.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import uz.bozorliq.R
import uz.bozorliq.databinding.DialogAddCategoryBinding
import uz.bozorliq.utils.blockClickable
import uz.bozorliq.utils.showKeyboard
import uz.bozorliq.utils.theme.ClassicTheme
import uz.bozorliq.utils.theme.ThemeManager

class AddCategoryDialog : AlertDialog {

    var cancel_listener_onclick: (() -> Unit)? = null
    var submit_listener_onclick: ((Int, String) -> Unit)? = null
    var binding: DialogAddCategoryBinding

    fun setOnSubmitClick(l: ((Int, String) -> Unit)?) {
        submit_listener_onclick = l
    }


    fun setOnCancelClick(l: (() -> Unit)?) {
        cancel_listener_onclick = l
    }

    constructor(context: Context, title: String, submitText: String, text: String? = null) : super(context) {
        this.setCancelable(true)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_category, null, false)
        binding = DialogAddCategoryBinding.bind(view)
        val theme = ThemeManager(binding.root.context).currentTheme

        binding.enterCategoryName.showKeyboard()

        if (theme.id == ClassicTheme().id) {
            binding.logoutBack.setCardBackgroundColor(theme.backgroundColor)
            binding.messageTittle.setTextColor(theme.colorPrimary)
            binding.add.setTextColor(theme.colorPrimary)
        } else {
            binding.logoutBack.setCardBackgroundColor(theme.colorPrimary)
            binding.messageTittle.setTextColor(theme.textColorPrimary)
            binding.add.setTextColor(theme.textColorPrimary)
        }
        binding.messageTittle.text = title
        binding.add.text = submitText
        text?.let {
            binding.enterCategoryName.setText(it)
        }

        binding.cancel.setTextColor(theme.textColor)
        binding.enterCategoryName.setTextColor(theme.textColor)

        view?.apply {
            binding.cancel.setOnClickListener {
                it.blockClickable()
                cancel_listener_onclick?.invoke()
            }
            binding.add.setOnClickListener {
                it.blockClickable()
                val name = binding.enterCategoryName.text.toString()
                if (name.isNotEmpty()) {
                    submit_listener_onclick?.invoke(1, name)
                } else {
                    submit_listener_onclick?.invoke(2, binding.root.context.getString(R.string.requared_category_name))
                }
            }
        }
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setView(view)
    }
}