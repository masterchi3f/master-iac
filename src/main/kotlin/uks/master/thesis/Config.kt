package uks.master.thesis

import java.io.File
import mu.KLogger
import mu.KotlinLogging

object Config {
    private val logger: KLogger = KotlinLogging.logger {}
    const val OUT_DIR = "out"

    fun deleteOutDir() = apply {
        if (File(OUT_DIR).deleteRecursively()) {
            logger.debug("Deleted directory $OUT_DIR")
        }
    }
}
