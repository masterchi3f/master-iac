package uks.master.thesis.terraform

import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import mu.KLogger
import mu.KotlinLogging
import uks.master.thesis.Config.OUT_DIR
import uks.master.thesis.terraform.syntax.elements.blocks.Provider
import uks.master.thesis.terraform.utils.Utils.createDir

object RootModule: ParentModule<RootModule>() {
    private val logger: KLogger = KotlinLogging.logger {}
    private const val MODULE_PREFIX = "module."
    private const val ROOT_NAME = "${MODULE_PREFIX}ROOT"
    private const val TERRAFORM_DIR = ".terraform"
    private const val GITIGNORE = ".gitignore"
    private const val GITIGNORE_RESOURCE_PATH = "/terraform/.tf${GITIGNORE}"
    private const val DOT_TF = ".tf"
    override val name: String get() = ROOT_NAME
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

    fun generateFiles(deleteOldFiles: Boolean = false) = apply {
        deleteOldFiles(deleteOldFiles)
        createDir(OUT_DIR, logger)
        createTfVarsFile()
        createGitignore()
        createFiles(OUT_DIR, toFileStrings())
        createSubModules(OUT_DIR, this)
    }

    override fun debugFiles() = apply {
        super.debugFiles()
        debugChildren(this)
    }

    private fun deleteOldFiles(deleteOldFiles: Boolean) {
        if (deleteOldFiles) {
            val files: Array<File>? = File(OUT_DIR).listFiles()
            files?.filter {
                it.name.endsWith(DOT_TF)
                    || it.name == TfVars.FILE_NAME
                    || it.name == GITIGNORE
            }?.forEach {
                if (it.delete()) {
                    logger.debug("Deleted old ${it.name}")
                }
            }
            files?.filter {
                it.isDirectory && it.name != TERRAFORM_DIR
            }?.forEach { submodule ->
                submodule.walkBottomUp().forEach {
                    if (// Is file or empty directory
                        it.walk().toList().size == 1
                        // Is directory or terraform file
                        && (it.isDirectory || it.name.endsWith(DOT_TF))
                        // Is successfully deleted
                        && it.delete()
                    ) {
                        logger.debug("Deleted old ${it.name}")
                    }
                }
            }
        }
    }

    private fun <T>createSubModules(parentPath: String, parentModule: ParentModule<T>) {
        for (child in parentModule.children) {
            if (child is SubModule) {
                val path = "$parentPath${File.separator}${child.name.removePrefix(MODULE_PREFIX)}"
                createDir(path, logger)
                createFiles(path, child.toFileStrings())
                createSubModules(path, child)
            }
        }
    }

    private fun createGitignore() {
        val url: URL? = javaClass.getResource(GITIGNORE_RESOURCE_PATH)
        url?.let {
            val resourceFile = File(it.path)
            val name: String = resourceFile.name.removePrefix(DOT_TF)
            val outFile = File(OUT_DIR, name)
            if (Files.notExists(Paths.get(outFile.toURI()))) {
                resourceFile.copyTo(outFile, false)
                logger.debug(
                    "Copied resource \"${GITIGNORE_RESOURCE_PATH.replace("/", File.separator)}\"" +
                    " to \"$OUT_DIR${File.separator}$name\""
                )
            }
        }
    }

    private fun createTfVarsFile() {
        var longestSpaces = 0
        TfVars.tfVars.forEach {
            val length: Int = it.key.length
            if (length > longestSpaces) {
                longestSpaces = length
            }
        }
        var content = ""
        TfVars.tfVars.forEach {
            val spaces: Int = longestSpaces - it.key.length
            content += "${it.key}${" ".repeat(spaces)} = \"${it.value}\"${System.lineSeparator()}"
        }
        val tfVars = File(OUT_DIR, TfVars.FILE_NAME)
        createFile(tfVars, content)
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
                child.name
                child.debugFiles()
                debugChildren(child)
            }
        }
    }
}
