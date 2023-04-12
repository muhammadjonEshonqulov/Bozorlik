package uz.bozorliq.utils.theme

import androidx.core.content.ContextCompat
import uz.bozorliq.R
import uz.bozorliq.app.App

class ClassicTheme : Theme() {
    override val id: Long
        get() = CLASSIC_THEME
    override val style: Int
        get() = R.style.ClassicTheme
    override val name: Int
        get() = R.string.day_mode
    override val colorPrimary: Int
        get() = ContextCompat.getColor(App.context, R.color.cl_color_primary)
    override val colorPrimaryDark: Int
        get() = ContextCompat.getColor(App.context, R.color.cl_color_primary_dark)
    override val colorAccent: Int
        get() = ContextCompat.getColor(App.context, R.color.cl_color_accent)
    override val navigationBarColor: Int
        get() = ContextCompat.getColor(App.context, R.color.white)
    override val backgroundColor: Int
        get() = ContextCompat.getColor(App.context, R.color.white)
    override val textColorPrimary: Int
        get() = ContextCompat.getColor(App.context, R.color.white)
    override val textColor: Int
        get() = ContextCompat.getColor(App.context, R.color.text_color)
    override val defTextColor: Int
        get() = ContextCompat.getColor(App.context, R.color.button_color)
    override val miniActionTextColor: Int
        get() = ContextCompat.getColor(App.context, R.color.mini_action_color)
}