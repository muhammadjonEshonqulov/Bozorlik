package uz.tsul.mobile.utils.language

abstract class Language {
    abstract val id: Int
    abstract val userName: String
    abstract val name: String

    companion object {
        const val UZ = 1
        const val EN = 2
        const val RU = 3

        fun getNameByLanguage(uz: String?, ru: String?, en: String?, language: Language): String? {
            return when (language.id) {
                UZ -> if (uz?.isNotEmpty() == true) uz else null
                EN -> if (en?.isNotEmpty() == true) en else if (uz?.isNotEmpty() == true) uz else null
                RU -> if (ru?.isNotEmpty() == true) ru else if (uz?.isNotEmpty() == true) uz else null
                else -> null
            }
        }
    }
}