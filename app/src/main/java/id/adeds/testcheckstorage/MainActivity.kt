package id.adeds.testcheckstorage

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import id.adeds.testcheckstorage.ui.theme.TestCheckStorageTheme
import java.io.File
import java.text.DecimalFormat
import java.util.*


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestCheckStorageTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(text = "free system: ${megabytesAvailable(systemPartitionDir())}")
                    Text(text = "internal: ${megabytesAvailable(internalDir())}")
                    Text(text = "external: ${megabytesAvailable(externalDir())}")
                    Text(text = "filesDir: ${megabytesAvailable(filesDir.path)}")
                    Text(text = "storageManager: ${checkStorageManager()}")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Activity.checkStorageManager(): String? {
    val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(applicationContext, null)
    val primaryExternalStorage = externalStorageVolumes[0]

    val storageManager = applicationContext.getSystemService<StorageManager>()
    val appSpecificInternalDirUuid = storageManager?.getUuidForPath(filesDir)
    return appSpecificInternalDirUuid?.let { bytesToHuman(storageManager.getAllocatableBytes(it).toDouble()) }
}

private fun megabytesAvailable(path: String): String {
    val stat = StatFs(path)
    val bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
    return bytesToHuman(bytesAvailable.toDouble())
}

private fun megabytesUsed(path: String): String {
    val stat = StatFs(path)
    val bytesAvailable = stat.blockSizeLong * stat.blockCountLong
    return bytesToHuman(bytesAvailable.toDouble())
}

private fun systemPartitionDir() = Environment.getRootDirectory().absolutePath
private fun internalDir() = Environment.getDataDirectory().absolutePath
private fun externalDir() = Environment.getExternalStorageDirectory().absolutePath

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

private fun floatForm(d: Double): String? {
    return DecimalFormat("#.##").format(d)
}