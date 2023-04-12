package uz.bozorliq.ui.settings.themes

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import uz.bozorliq.base.BaseFragment
import uz.bozorliq.databinding.FragmentThemeBinding
import uz.bozorliq.utils.Prefs
import uz.bozorliq.utils.theme.ClassicTheme
import uz.bozorliq.utils.theme.NightTheme
import uz.bozorliq.utils.theme.Theme
import uz.bozorliq.utils.theme.ThemeManager
import javax.inject.Inject

@AndroidEntryPoint
class ThemeFragment : BaseFragment<FragmentThemeBinding>(FragmentThemeBinding::inflate), View.OnClickListener {

    @Inject
    lateinit var prefss: Prefs

    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        themeManager = ThemeManager(requireContext())
        binding.backBtnTheme.setOnClickListener(this)
        binding.nightMode.setOnClickListener(this)
        binding.dayMode.setOnClickListener(this)
        val theme: Long = prefss.get(prefss.theme, 0L)
        if (theme == 1L) {
            binding.dayModeSelected.visibility = View.GONE
            binding.nightModeSelected.visibility = View.VISIBLE
        } else {
            binding.dayModeSelected.visibility = View.VISIBLE
            binding.nightModeSelected.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.dayMode -> {
                activeClassicMode()
                prefss.save(prefss.theme, 0L)
                binding.dayModeSelected.visibility = View.VISIBLE
                binding.nightModeSelected.visibility = View.GONE
                //  closeAllFragmentsAndOpenThis(MainFragment(),isAnimate = true)
            }
            binding.nightMode -> {
                activeNightMode()
                prefss.save(prefss.theme, 1L)
                binding.dayModeSelected.visibility = View.GONE
                binding.nightModeSelected.visibility = View.VISIBLE
                //closeAllFragmentsAndOpenThis(MainFragment(),isAnimate = true)
            }
            binding.backBtnTheme -> {
                finish()
            }
        }
    }

    private fun activeNightMode() {
        if (themeManager.currentTheme.id == Theme.NIGHT_THEME)
            return

        themeManager.currentTheme = NightTheme()
        notifyThemeChanged()

    }

    private fun activeClassicMode() {
        if (themeManager.currentTheme.id == Theme.CLASSIC_THEME)
            return

        themeManager.currentTheme = ClassicTheme()
        notifyThemeChanged()
    }

    override fun onCreateTheme(theme: Theme) {
        super.onCreateTheme(theme)

        binding.actionBarTheme.setBackgroundColor(theme.colorPrimary)
        binding.backBtnTheme.setColorFilter(theme.textColorPrimary, PorterDuff.Mode.MULTIPLY)
        binding.titleTheme.setTextColor(theme.textColorPrimary)
        binding.dayModeTxt.setTextColor(theme.textColor)
        binding.nightModeTxt.setTextColor(theme.textColor)
        binding.fragmentTheme.setBackgroundColor(theme.backgroundColor)
    }
}