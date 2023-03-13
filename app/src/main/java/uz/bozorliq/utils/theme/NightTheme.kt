package uz.bozorliq.utils.theme

import androidx.core.content.ContextCompat
import uz.bozorliq.R
import uz.bozorliq.app.App

class NightTheme : Theme() {
    override val id: Long
        get() = NIGHT_THEME
    override val style: Int
        get() = R.style.NightTheme
    override val name: Int
        get() = R.string.night_mode
    override val colorPrimary: Int
        get() = ContextCompat.getColor(App.context, R.color.n_color_primary)
    override val colorPrimaryDark: Int
        get() = ContextCompat.getColor(App.context, R.color.n_color_primary_dark)
    override val colorAccent: Int
        get() = ContextCompat.getColor(App.context, R.color.n_color_accent)
    override val navigationBarColor: Int
        get() = ContextCompat.getColor(App.context, R.color.n_color_primary)
    override val backgroundColor: Int
        get() = ContextCompat.getColor(App.context, R.color.n_background)
    override val textColorPrimary: Int
        get() = ContextCompat.getColor(App.context, R.color.white)
    override val textColor: Int
        get() = ContextCompat.getColor(App.context, R.color.n_text_color)
    override val defTextColor: Int
        get() = ContextCompat.getColor(App.context, R.color.n_def_text_color)
    override val miniActionTextColor: Int
        get() = ContextCompat.getColor(App.context, R.color.n_color_primary_dark)
}