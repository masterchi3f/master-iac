package uks.master.thesis.terraform

import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Executors
import java.util.zip.ZipInputStream
import mu.KLogger
import mu.KotlinLogging
import uks.master.thesis.Config.OUT_DIR
import uks.master.thesis.terraform.utils.Utils.createDir

object Executor {
    private val logger: KLogger = KotlinLogging.logger {}
    var terraformCommand: String? = null
        private set

    /**
     * Prepare your working directory for other commands
     */
    fun init() = apply {
        runCommand("init")
    }

    /**
     * Check whether the configuration is valid
     */
    fun validate() = apply {
        runCommand("validate")
    }

    /**
     * Show changes required by the current configuration
     */
    fun plan() = apply {
        val command = "plan ${getVarFile()}"
        runCommand(command)
    }

    /**
     * Create or update infrastructure
     */
    fun apply() = apply {
        val command = "apply -auto-approve ${getVarFile()}"
        runCommand(command)
    }

    /**
     * Destroy previously-created infrastructure
     */
    fun destroy() = apply {
        val command = "destroy -auto-approve ${getVarFile()}"
        runCommand(command)
    }

    /**
     * List all available commands
     */
    fun help() = apply {
        runCommand("--help")
    }

    /**
     * Usage: terraform [global options] <subcommand> [args]
     */
    fun command(
        subcommand: String,
        args: List<String> = listOf(),
        globalOptions: List<String> = listOf()
    ) = apply {
        var command = ""
        globalOptions.forEach {
            command += "$it "
        }
        command += subcommand
        args.forEachIndexed { index, it ->
            if (index == 0) {
                command += " "
            }
            command += it
            if (index != args.size - 1) {
                command += " "
            }
        }
        runCommand(command)
    }

    fun downloadTerraformBinary(url: String) = apply {
        terraformCommand = downloadBinary(url)
    }

    private fun getVarFile(): String = "-var-file=\"${TfVars.FILE_NAME}\""

    private fun runCommand(command: String) {
        terraformCommand ?: run {
            if (!doesTerraformCommandExist()) {
                logger.warn("Terraform binary was not downloaded!")
                return
            }
        }
        val processBuilder = ProcessBuilder()
        val process: Process = processBuilder
            .directory(File(OUT_DIR))
            .command(getInterpreter() + ".${File.separator}$terraformCommand $command")
            .redirectErrorStream(true)
            .start()
        Executors.newSingleThreadExecutor().submit {
            logger.debug(
                process.inputStream.readAllBytes().decodeToString()
            )
        }
        process.waitFor()
    }

    private fun doesTerraformCommandExist(): Boolean {
        val name = "terraform" + if (onWindows()) ".exe" else ""
        val binary = File(OUT_DIR, name)
        val exist: Boolean = Files.exists(Paths.get(binary.toURI()))
        if (exist) {
            terraformCommand = name
        }
        return exist
    }

    private fun getInterpreter(): MutableList<String> {
        return if (onWindows()) {
            mutableListOf("cmd.exe", "/c")
        } else {
            mutableListOf("/bin/sh", "-c")
        }
    }

    private fun onWindows(): Boolean = System
        .getProperty("os.name")
        .contains("windows", true)

    /**
     * This method suggests that the binaries from
     * https://developer.hashicorp.com/terraform/downloads
     * all have the same file hierarchy like the following:
     * terraform_<version, OS, type>.zip -> terraform binary (no folders or additional files in zip-archive)
     * It downloads the zip-archive and decompresses the binary to the "out" folder in the root directory.
     */
    private fun downloadBinary(url: String): String? {
        val website = URL(url)
        website.openStream().use { inputStream ->
            ZipInputStream(inputStream).use { zipInputStream ->
                zipInputStream.nextEntry.let { nextEntry ->
                    nextEntry?.let { zipEntry ->
                        createDir(OUT_DIR, logger)
                        val binary = File(OUT_DIR, zipEntry.name)
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
}
