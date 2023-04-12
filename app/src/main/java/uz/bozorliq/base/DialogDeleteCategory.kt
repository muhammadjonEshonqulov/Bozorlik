package uz.bozorliq.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import uz.bozorliq.R
import uz.bozorliq.databinding.DialogDeleteCategryBinding
import uz.bozorliq.utils.blockClickable
import uz.bozorliq.utils.theme.ThemeManager

class DialogDeleteCategory : AlertDialog {

    var no_listener_onclick: (() -> Unit)? = null
    var yes_listener_onclick: (() -> Unit)? = null
    var binding: DialogDeleteCategryBinding

    fun setOnSubmitClick(l: (() -> Unit)?) {
        yes_listener_onclick = l
    }

    fun setOnCancelClick(l: (() -> Unit)?) {
        no_listener_onclick = l
    }

    @SuppressLint("SetTextI18n")
    constructor(context: Context, title: String, text: String) : super(context) {
        this.setCancelable(true)

        binding = DialogDeleteCategryBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_delete_categry, null, false))

        val theme = ThemeManager(binding.root.context).currentTheme
        binding.back.setCardBackgroundColor(theme.backgroundColor)
        binding.tittle.setTextColor(theme.textColor)
        binding.message.setTextColor(theme.textColor)

        binding.tittle.text = title
        binding.message.text = text


        binding.yes.setOnClickListener {
            it.blockClickable()
            yes_listener_onclick?.invoke()
        }
        binding.no.setOnClickListener {
            it.blockClickable()
            no_listener_onclick?.invoke()
        }
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setView(binding.root)

    }
}