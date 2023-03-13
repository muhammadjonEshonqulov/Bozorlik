package uz.bozorliq.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import uz.bozorliq.BuildConfig
import java.io.File
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


fun snackBarAction(binding: ViewBinding, message: String) {
    binding.root.let {
        Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE).also { snackbar ->
            snackbar.setAction("ok") {
                snackbar.dismiss()
            }
        }.show()
    }
}

fun <T> SharedFlow<T>.setValue() {

}

suspend fun <T> SharedFlow<T>.emit(value: T) {
    (this as? MutableSharedFlow<T>)?.tryEmit(value)
//    (this as? MutableSharedFlow<T>)?.emit(value)
}

suspend fun <T> LiveData<T>.emit(value: T) {
    (this as? MutableLiveData<T>)?.emit(value)
}

fun <T> publishFlow(): MutableSharedFlow<T> {
    return MutableSharedFlow(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
}

fun ViewModel.launchCatching(
    action: suspend CoroutineScope.() -> Unit,
    exceptionAction: (suspend (Exception) -> Unit)? = null,
    finalAction: (suspend () -> Unit)? = null,
) {
    viewModelScope.launch {
        try {
            action()
        } catch (e: Exception) {
            exceptionAction?.invoke(e)
        } finally {
            finalAction?.invoke()
        }
    }
}

fun <T> debounce(
    delayMs: Long = 5000L,
    coroutineContext: CoroutineContext,
    f: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob?.isCompleted != false) {
            debounceJob = CoroutineScope(coroutineContext).launch {
                delay(delayMs)
                f(param)
            }
        }
    }
}

/**
 * #Convenience
 *
 * Used to retry the [block] function [numOfRetries] times until it succeeded
 */
suspend fun <T> retry(numOfRetries: Long, block: suspend () -> T) {
    var throwable: Throwable? = null
    (1..numOfRetries).forEach { attempt ->
//        try {
        block()
//        } catch (e: Throwable) {
//            throwable = e
//            println("Failed attempt $attempt / $numOfRetries")
//        }
    }
//    return block()
//    throw throwable!!

}

fun snackBar(binding: ViewBinding, message: String) {
    try {
        binding.root.let {
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    } catch (e: Exception) {

    }
}

private fun get64LeastSignificantBitsForVersion1(): Long {
    val random63BitLong = Random().nextLong() and 0x3FFFFFFFFFFFFFFFL
    val variant3BitFlag = (0x3FFFFFFFFFFFFFFFL).toLong()
    return random63BitLong + variant3BitFlag
}

fun deleteFiles(path: String) {
    val file = File(path)
    if (file.exists()) {
        val deleteCmd = "rm -r $path"
        val runtime = Runtime.getRuntime()
        try {
            runtime.exec(deleteCmd)
        } catch (e: IOException) {
        }
    }
}

fun changeTypeDate(date: String): String {

    val dayCurrent = getNow().split(".").first().toInt()
    val monthCurrent = getNow().split(".").get(1).toInt()
    val yearCurrent = getNow().split(".").last().toInt()

    val day = date.split("-").last().toInt()
    val month = date.split("-").get(1).toInt()
    val year = date.split("-").first().toInt()

    val availableAge = 18 * 365 // 6,205
    val differ = (yearCurrent - year) * 365 + (((monthCurrent - 1) * 30 + dayCurrent) - ((month - 1) * 30 + day))

    return if (differ > availableAge) {
        if (day > 9 && month > 9) {
            "$day.$month.$year"
        } else {
            if (day < 10 && month < 10) {
                "0$day.0$month.$year"
            } else {
                if (day < 10)
                    "0$day.$month.$year"
                else
                    "$day.0$month.$year"
            }

        }
    } else {
        ""
    }
}

fun finish(binding: ViewBinding) {
    binding.root.findNavController().popBackStack()
}

fun <T> finish(binding: ViewBinding, key: String, value: T) {
    val navController = binding.root.findNavController()
    navController.previousBackStackEntry?.savedStateHandle?.set(key, value)
    navController.popBackStack()
}

fun lg(message: Any) {
    if (BuildConfig.isDebug) {
        Log.d("MY_LOG", "$message")
    }
}

fun toast(binding: ViewBinding, message: String) {
    Toast.makeText(binding.root.context, message, Toast.LENGTH_LONG).show()

}

@SuppressLint("SimpleDateFormat")
fun getNow(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    val currentDate = sdf.format(Date())
    return currentDate
}

fun Fragment.findNavControllerSafely(): NavController? {
    return if (isAdded) {
        findNavController()
    } else {
        null
    }
}

fun Activity.findNavControllerSafely(fragment: Fragment): NavController? {
    return findNavControllerSafely(fragment)
}

fun String.toDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(this)
}

fun URL.getFileSize(): Int? {
    return try {
        openConnection().contentLength
    } catch (x: Exception) {
        null
    }
}

@SuppressLint("NewApi", "ObsoleteSdkInt")
fun String.decode(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
        android.util.Base64.decode(this, android.util.Base64.DEFAULT).toString(charset("UTF-8"))
    } else {
        TODO("VERSION.SDK_INT < FROYO")
    }
}

@SuppressLint("NewApi", "ObsoleteSdkInt")
fun ByteArray.encode(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
        android.util.Base64.encode(this, android.util.Base64.DEFAULT).toString(charset("UTF-8"))
    } else {
        TODO("VERSION.SDK_INT < FROYO")
    }
}

fun getRootDirPath_(context: Context): String? {
    return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
        val file = ContextCompat.getExternalFilesDirs(context.applicationContext, null)[0]
        file.absolutePath
    } else {
        context.applicationContext.filesDir.absolutePath
    }
}

fun TextView.ellipsizeText() {
    isSingleLine = true
    maxLines = 1
    ellipsize = TextUtils.TruncateAt.END
}

fun String.getTime() = this.split(" ")[1].substring(0, 5)

fun Int.getDuration(): String {
    val a = this / 3600
    val b = this / 60
    val h = if (a <= 9) "0$a" else (a).toString()
    val m = if (b <= 9) "0${b}" else (b).toString()
    return "$h:$m"
}

@SuppressLint("SimpleDateFormat")
fun Int.getDate(): String {
    val c: Calendar = GregorianCalendar()
    c.time = Date()
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    c.add(Calendar.MONTH, -this)
    return sdf.format(c.time)
}


fun EditText.showKeyboard() {
    post {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun BottomNavigationView.disableTooltip() {

    val count: Int = this.childCount;
    if (count <= 0) {
        return
    }

    val menuView: ViewGroup = this.getChildAt(0) as ViewGroup

    val menuItemViewSize: Int = menuView.childCount
    if (menuItemViewSize <= 0) {
        return
    }

    for (i in 0 until menuItemViewSize) {
        menuView.getChildAt(
            i
        ).setOnLongClickListener { true }
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View?.blockClickable(
    blockTimeMilles: Long = 500
) {
    this?.isClickable = false
    Handler().postDelayed({ this?.isClickable = true }, blockTimeMilles)
}
//class Request {

private var requestTimestamp = 0L

fun listen() {
    if (!shouldRequest()) return
}

private fun shouldRequest(): Boolean {
    val currentTimestamp = System.currentTimeMillis()
    if (currentTimestamp - requestTimestamp < 3000L) return false
    requestTimestamp = currentTimestamp
    return true
}