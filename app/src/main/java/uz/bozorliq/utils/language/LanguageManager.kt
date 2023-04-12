package uz.bozorliq.utils.language

import android.content.Context
import uz.bozorliq.utils.Prefs
import uz.tsul.mobile.utils.language.Language
import uz.tsul.mobile.utils.language.Russian
import uz.tsul.mobile.utils.language.Uzbek

class LanguageManager(val context: Context) {

    var languages: ArrayList<Language> = ArrayList()
    val prefs = Prefs(context)

    init {
        languages.add(Uzbek())
        languages.add(English())
        languages.add(Russian())
    }

    var currentLanguage: Language
        get() = findLanguageById(prefs.get(prefs.language, languages[0].id))
        set(value) = prefs.save(prefs.language, value.id)

    fun findLanguageById(id: Int): Language {
        languages.forEach {
            if (it.id == id)
                return it
        }
        return languages[0]
    }

}