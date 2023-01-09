package id.adeds.testcheckstorage

import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.adeds.testcheckstorage.ui.theme.TestCheckStorageTheme
import java.text.DecimalFormat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestCheckStorageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(megabytesAvailable())
                }
            }
        }
    }
}

fun megabytesAvailable(): String {
    val stat = StatFs(Environment.getDataDirectory().absolutePath)
    val bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
    return bytesToHuman(bytesAvailable.toDouble())
}

fun bytesToHuman(size: Double): String {
    val Kb = (1 * 1024).toLong()
    val Mb = Kb * 1024
    val Gb = Mb * 1024
    val Tb = Gb * 1024
    val Pb = Tb * 1024
    val Eb = Pb * 1024
    if (size < Kb) return floatForm(size) + " byte"
    if (size >= Kb && size < Mb) return floatForm(size / Kb) + " Kb"
    if (size >= Mb && size < Gb) return floatForm(size / Mb) + " Mb"
    if (size >= Gb && size < Tb) return floatForm(size / Gb) + " Gb"
    if (size >= Tb && size < Pb) return floatForm(size / Tb) + " Tb"
    if (size >= Pb && size < Eb) return floatForm(size / Pb) + " Pb"
    return if (size >= Eb) floatForm(size / Eb) + " Eb" else "???"
}

fun floatForm(d: Double): String? {
    return DecimalFormat("#.##").format(d)
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestCheckStorageTheme {
        Greeting("Android")
    }
}