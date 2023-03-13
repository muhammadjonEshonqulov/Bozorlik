package uz.bozorliq.ui.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.bozorliq.R
import uz.bozorliq.base.BaseFragment
import uz.bozorliq.databinding.FragmentSplashBinding
import uz.bozorliq.utils.Prefs
import uz.bozorliq.utils.navigateSafe
import uz.bozorliq.utils.theme.ClassicTheme
import uz.bozorliq.utils.theme.Theme
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {


        object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                findNavController().navigateSafe(R.id.action_splashFragment_to_student_mainFragment)
            }
        }.start()
    }


    override fun onCreateTheme(theme: Theme) {
        super.onCreateTheme(theme)
        binding.itemBack.setBackgroundColor(theme.backgroundColor)
        if (theme.id == ClassicTheme().id) {
            binding.text.setTextColor(theme.colorPrimary)
            return
        }
        binding.text.setTextColor(theme.textColor)

    }
}