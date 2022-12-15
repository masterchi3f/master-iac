package uks.master.thesis.terraform

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import mu.KotlinLogging
import uks.master.thesis.terraform.syntax.elements.blocks.Provider

object RootModule: ParentModule<RootModule>() {
    private val logger = KotlinLogging.logger {}
    private const val OUT_DIR = "out"
    private const val MODULE_PREFIX = "module."
    var providers: List<Provider> = mutableListOf()
        private set

    fun addProvider(provider: Provider) = apply { providers = providers + provider }

    override fun toFileStrings(): ModuleFiles {
        val result = super.toFileStrings()
        var main: String = result.main + System.lineSeparator()
        providers.forEach {
            main += it.toString() + System.lineSeparator()
        }
        return ModuleFiles(
            main.removeSuffix(System.lineSeparator()),
            result.variables,
            result.outputs
        )
    }

    fun generateFiles() = apply {
        createDir(OUT_DIR)
        createFiles(OUT_DIR, toFileStrings())
        createSubModules(OUT_DIR, this)
    }

    override fun debugFiles() = apply {
        super.debugFiles()
        debugChildren(this)
    }

    private fun <T>createSubModules(parentPath: String, parentModule: ParentModule<T>) {
        for (child in parentModule.children) {
            if (child is SubModule) {
                val path = "$parentPath${File.separator}${child.name().removePrefix(MODULE_PREFIX)}"
                createDir(path)
                createFiles(path, child.toFileStrings())
                createSubModules(path, child)
            }
        }
    }

    private fun createFiles(parentPath: String, moduleFiles: ModuleFiles) {
        val main = File(parentPath, MAIN)
        createFile(main, moduleFiles.main)
        moduleFiles.variables?.let {
            val variables = File(parentPath, VARIABLES)
            createFile(variables, it)
        }
        moduleFiles.outputs?.let {
            val outputs = File(parentPath, OUTPUTS)
            createFile(outputs, it)
        }
    }

    private fun createFile(file: File, content: String) {
        if (Files.notExists(Paths.get(file.toURI()))) {
            file.writeText(content)
            logger.debug("Created file \"${file.path}\"")
        }
    }

    private fun <T>debugChildren(parentModule: ParentModule<T>) {
        for (child in parentModule.children) {
            if (child is SubModule) {
                child.name()
                child.debugFiles()
                debugChildren(child)
            }
        }
    }

    private fun createDir(name: String) {
        val dir = File(name)
        if (dir.exists()) {
            return
        }
        if (!dir.mkdirs()) {
            throw IOException("Failed to create directory \"${name}\"")
        }
        logger.debug("Created directory \"${name}\"")
    }

    override fun name(): String = "module.ROOT"
}
