package uz.bozorliq.utils.theme

import android.content.Context
import uz.bozorliq.utils.Prefs

class ThemeManager(val context: Context) {

    var themes: ArrayList<Theme> = ArrayList()
    val prefss = Prefs(context)

    init {
        themes.add(ClassicTheme())
        themes.add(NightTheme())
    }

    var currentTheme: Theme
        get() = findThemeById(prefss.get(prefss.theme, getDefaultTheme().id))
        set(value) {
            prefss.save(prefss.theme, value.id)
        }

    private fun findThemeById(id: Long): Theme {
        themes.forEach {
            if (it.id == id)
                return it
        }
        return getDefaultTheme()
    }

    private fun getDefaultTheme(): Theme {
        return themes[0]
    }

    fun getAllThemes() = themes
}