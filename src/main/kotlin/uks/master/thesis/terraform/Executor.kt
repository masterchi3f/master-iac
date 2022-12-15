package uks.master.thesis.terraform

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipInputStream
import mu.KotlinLogging

object Executor {

    private const val BINARIES_DIR = "binaries"
    private val logger = KotlinLogging.logger {}
    var terraformCommand: String? = null

    fun downloadTerraformBinary(url: String) {
        terraformCommand = downloadBinary(url)
    }

    /**
     * This method suggests that the binaries from
     * https://developer.hashicorp.com/terraform/downloads
     * all have the same file hierarchy like the following:
     * terraform_<version, OS, type>.zip -> terraform binary (no folders or additional files in zip-archive)
     * It downloads the zip-archive and decompresses the binary to a "binaries" folder in the root directory.
     */
    private fun downloadBinary(url: String): String? {
        val website = URL(url)
        website.openStream().use { inputStream ->
            ZipInputStream(inputStream).use { zipInputStream ->
                zipInputStream.nextEntry.let { nextEntry ->
                    nextEntry?.let { zipEntry ->
                        val binary = File(BINARIES_DIR, zipEntry.name)
                        createBinariesDir(binary)
                        if (Files.notExists(Paths.get(binary.toURI()))) {
                            Channels.newChannel(zipInputStream).use { readableByteChannel ->
                                FileOutputStream(binary).use { fileOutputStream ->
                                    fileOutputStream.channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
                                }
                            }
                            logger.debug("Downloaded and unzipped \"${zipEntry.name}\"")
                        }
                        return zipEntry.name
                    }
                }
            }
        }
        return null
    }

    private fun createBinariesDir(binary: File) {
        val parent: File = binary.parentFile
        if (parent.exists()) {
            return
        }
        if (!parent.mkdirs()) {
            throw IOException("Failed to create directory \"$BINARIES_DIR\"")
        }
        logger.debug("Created directory \"$BINARIES_DIR\"")
    }
}
