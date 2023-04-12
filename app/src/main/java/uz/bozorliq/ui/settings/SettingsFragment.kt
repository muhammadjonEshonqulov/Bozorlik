package uz.bozorliq.ui.settings

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.bozorliq.R
import uz.bozorliq.base.BaseFragment
import uz.bozorliq.databinding.FragmentSettingsBinding
import uz.bozorliq.utils.Prefs
import uz.bozorliq.utils.blockClickable
import uz.bozorliq.utils.navigateSafe
import uz.bozorliq.utils.theme.Theme
import uz.tsul.mobile.utils.language.Language
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate), View.OnClickListener {

    @Inject
    lateinit var prefss: Prefs


    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {

        binding.backBtnSettings.setOnClickListener(this)
        binding.themes.setOnClickListener(this)
        binding.language.setOnClickListener(this)
        val theme: Long = prefss.get(prefss.theme, 0L)

        if (theme == 1L) {
            binding.themesTextPos.text = getString(R.string.night_mode)
        } else {
            binding.themesTextPos.text = getString(R.string.day_mode)
        }

        when (prefss.get(prefss.language, Language.UZ)) {
            Language.UZ -> {
                binding.languageTextPos.text = getString(R.string.uzbek_language)
            }
            Language.RU -> {
                binding.languageTextPos.text = getString(R.string.russian_language)
            }
            Language.EN -> {
                binding.languageTextPos.text = getString(R.string.english_language)
            }
        }
    }

    override fun onCreateTheme(theme: Theme) {
        super.onCreateTheme(theme)
        binding.actionBarSettings.setBackgroundColor(theme.colorPrimary)
        binding.backBtnSettings.setColorFilter(theme.textColorPrimary, PorterDuff.Mode.MULTIPLY)
        binding.titleSettings.setTextColor(theme.textColorPrimary)
        binding.themesText.setTextColor(theme.textColor)
        binding.languageText.setTextColor(theme.textColor)
        binding.fragmentSettings.setBackgroundColor(theme.backgroundColor)
    }

    override fun onClick(v: View?) {
        v.blockClickable()
        when (v?.id) {
            R.id.back_btn_settings -> {
                hideKeyBoard()
                finish()
            }
            R.id.themes -> {
                findNavController().navigateSafe(R.id.action_settingsFragment_to_themeFragment)
            }
            R.id.language -> {
                findNavController().navigateSafe(R.id.action_settingsFragment_to_languageFragment)
            }
        }
    }
}