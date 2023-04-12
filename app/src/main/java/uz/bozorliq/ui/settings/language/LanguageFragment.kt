package uz.bozorliq.ui.settings.language

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import uz.bozorliq.R
import uz.bozorliq.base.BaseFragment
import uz.bozorliq.databinding.FragmentLanguageBinding
import uz.bozorliq.utils.Prefs
import uz.bozorliq.utils.language.LanguageManager
import uz.bozorliq.utils.theme.Theme
import uz.bozorliq.utils.language.English
import uz.tsul.mobile.utils.language.Language
import uz.tsul.mobile.utils.language.Russian
import uz.tsul.mobile.utils.language.Uzbek
import javax.inject.Inject

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate), View.OnClickListener {

    @Inject
    lateinit var prefss: Prefs


    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        binding.backBtnLanguage.setOnClickListener(this)
        binding.lanUz.setOnClickListener(this)
        binding.lanRu.setOnClickListener(this)
        binding.lanEn.setOnClickListener(this)
    }

    private fun setLanguage(language: Int) {

        val imageViews = ArrayList<ImageView>()
        imageViews.add(binding.langUzSelected)
        imageViews.add(binding.lanEnSelected)
        imageViews.add(binding.lanRuSelected)

        LanguageManager(requireContext()).languages.forEachIndexed { index, languageData ->
            if (languageData.id == language) {
                imageViews[index].visibility = View.VISIBLE
            } else {
                imageViews[index].visibility = View.GONE
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtnLanguage -> {
                finish()
            }
            binding.lanUz -> {
                prefss.save(prefss.language, Language.UZ)
                languageManager.currentLanguage = Uzbek()
                setLanguage(Language.UZ)
                notifyLanguageChanged()
            }
            binding.lanRu -> {
                prefss.save(prefss.language, Language.RU)
                languageManager.currentLanguage = Russian()
                setLanguage(Language.RU)
                notifyLanguageChanged()
            }
            binding.lanEn -> {
                prefss.save(prefss.language, Language.EN)
                languageManager.currentLanguage = English()
                setLanguage(Language.EN)
                notifyLanguageChanged()
            }
        }
    }

    override fun onCreateTheme(theme: Theme) {
        super.onCreateTheme(theme)
        binding.actionBarLanguage.setBackgroundColor(theme.colorPrimary)
        binding.backBtnLanguage.setColorFilter(theme.textColorPrimary, PorterDuff.Mode.MULTIPLY)
        binding.titleLanguage.setTextColor(theme.textColorPrimary)
        binding.langUzTxt.setTextColor(theme.textColor)
        binding.langEnTxt.setTextColor(theme.textColor)
        binding.langRuTxt.setTextColor(theme.textColor)
        binding.fragmentLanguage.setBackgroundColor(theme.backgroundColor)

    }

    override fun onCreateLanguage(language: Language) {
        super.onCreateLanguage(language)
        binding.titleLanguage.text = getString(R.string.languages)
        setLanguage(language.id)
    }
}