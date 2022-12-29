package uks.master.thesis.terraform.utils

import java.io.File
import java.io.IOException
import mu.KLogger

object Utils {
    fun convertToIdentifier(reference: String): String =
        reference.replace(".", "_")

    fun splatExp(blockType: String, i: Int? = null, attr: String? = null): String {
        i ?: attr ?: return blockType
        return "$blockType[${i?.toString() ?: "*"}]${attr?.let { ".$it" } ?: ""}"
    }

    fun createDir(name: String, logger: KLogger? = null) {
        val dir = File(name)
        if (dir.exists()) {
            return
        }
        if (!dir.mkdirs()) {
            throw IOException("Failed to create directory \"${name}\"")
        }
        logger?.debug("Created directory \"${name}\"")
    }
}
