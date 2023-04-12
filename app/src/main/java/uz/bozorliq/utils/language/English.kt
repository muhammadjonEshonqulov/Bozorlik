package uz.bozorliq.utils.language

import uz.tsul.mobile.utils.language.Language

class English :
    Language() {
    override val id: Int
        get() = EN
    override val userName: String
        get() = "en"
    override val name: String
        get() = "English"
}