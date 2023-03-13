package uz.bozorliq.base

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import uz.bozorliq.app.App
import uz.bozorliq.utils.lg
import uz.bozorliq.utils.theme.Theme
import uz.bozorliq.utils.theme.ThemeManager
import uz.tsul.mobile.utils.language.Language
import uz.bozorliq.utils.language.LanguageManager
import java.util.*

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {
    private var isUseBackPress = true
    lateinit var languageManager: LanguageManager
    lateinit var themeManager: ThemeManager

//    lateinit var textSizes: TextSizes


    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageManager = LanguageManager(App.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        binding.root.context?.let {
            languageManager = LanguageManager(it)
            themeManager = ThemeManager(it)
//            textSizes = TextSizes(it)

        }
        return binding.root
    }

//    var loaderInterfacee: LoaderInterface? = null
//
//    override fun onAttach(activity: Activity) {
//        super.onAttach(activity)
//        try {
//            loaderInterfacee = activity as LoaderInterface
//        } catch (e: ClassCastException) {
//            throw ClassCastException("$activity error")
//        }
//    }

//    fun closeLoader() {
//        loaderInterfacee?.closeLoader()
//    }
//
//    fun showLoader() {
//        loaderInterfacee?.showLoader()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, e ->
            if (keyCode == KeyEvent.KEYCODE_BACK && e.action == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.FLAG_SOFT_KEYBOARD) {
                isUseBackPress = true
                onBackPressed()
                return@setOnKeyListener isUseBackPress
            } else return@setOnKeyListener false
        }
        onViewCreatedd(view, savedInstanceState)
        notifyLanguageChanged()
        notifyThemeChanged()
//        notifyOnTextSize()
    }

    abstract fun onViewCreatedd(view: View, savedInstanceState: Bundle?)

    open fun onBackPressed() {
        isUseBackPress = false
    }

    fun finish() {
        findNavController().popBackStack()
    }

    fun <T> finish(key: String, value: T) {
        val navController = findNavController()
        navController.previousBackStackEntry?.savedStateHandle?.set(key, value)
        navController.popBackStack()
    }

    fun hideKeyBoard() {
        val view = activity?.currentFocus ?: View(activity)
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyBoard(view: EditText) {
//        val view = activity?.currentFocus ?: View(activity)
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun snackBar(message: String) {
        try {
            binding.root.let {
                val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                val textView: TextView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text)
                textView.maxLines = 4
                snackbar.show()
            }
        } catch (e: Exception) {
            lg("error in snackbar " + e.message.toString())
        }
    }

//    fun snackBarAction(message: String) {
//        try {
//            binding.root.let {
//                val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE)
//                snackbar.setAction(it.context.getString(R.string.close)) {
//                    snackbar.dismiss()
//                }
//                val textView: TextView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text)
//                textView.maxLines = 6
//                snackbar.show()
//            }
//        } catch (e: Exception) {
//
//        }
//    }

    private fun notifyThemeChanged() = onCreateTheme(themeManager.currentTheme)
    private fun notifyLanguageChanged() = onCreateLanguage(languageManager.currentLanguage)
//    protected fun notifyOnTextSize() = onTextSiz(textSizes)

    open fun onCreateTheme(theme: Theme) {

        if (theme.id == Theme.CLASSIC_THEME) {
            val statusBarColor = theme.colorPrimaryDark
            val navigationBarColor = theme.navigationBarColor
            activity?.window?.statusBarColor = statusBarColor
            activity?.window?.navigationBarColor = navigationBarColor
            activity?.window?.decorView?.let { view ->
                view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            val statusBarColor = theme.colorPrimaryDark
            val navigationBarColor = theme.navigationBarColor
            activity?.window?.statusBarColor = statusBarColor
            activity?.window?.navigationBarColor = navigationBarColor
            activity?.window?.decorView?.let { view ->
                view.systemUiVisibility = 0
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (theme.id == Theme.CLASSIC_THEME) {
                val navigationBarColor = theme.navigationBarColor
                activity?.window?.navigationBarColor = navigationBarColor
                activity?.window?.decorView?.let { view ->
                    view.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            } else {
                val navigationBarColor = theme.navigationBarColor
                activity?.window?.navigationBarColor = navigationBarColor
                activity?.window?.decorView?.let { view ->
                    view.systemUiVisibility = 0
                }
            }
        }
    }

    open fun onCreateLanguage(language: Language) {
        App.context = binding.root.context
        _binding?.root.apply {
            val configuration = Configuration()
            configuration.setLocale(
                Locale(languageManager.currentLanguage.userName)
            )
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }
}