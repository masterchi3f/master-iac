package uks.master.thesis.terraform

import mu.KLogger
import mu.KotlinLogging
import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.elements.OneLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineSymbol
import uks.master.thesis.terraform.syntax.elements.blocks.InputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.OutputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.Terraform
import uks.master.thesis.terraform.syntax.elements.blocks.TfModule

@Suppress("UNCHECKED_CAST")
abstract class ParentModule<T> {
    protected companion object {
        const val VARIABLES = "variables.tf"
        const val OUTPUTS = "outputs.tf"
        const val MAIN = "main.tf"
    }
    private val logger: KLogger = KotlinLogging.logger {}
    private var configuration: Terraform? = null
    data class ModuleFiles(val main: String, val variables: String?, val outputs: String?)
    var children: List<Child> = mutableListOf()
        private set

    fun setConfiguration(terraform: Terraform): T = apply {
        configuration = terraform
    } as T

    fun addChild(child: Child): T = apply {
        if (child is TfModule && child.type == TfModule.Type.LOCAL) {
            throw IllegalArgumentException(
                "Found TfModule from type LOCAL. Wrap local modules to be SubModules."
            )
        }
        children = children + child
    } as T

    open fun toFileStrings(): ModuleFiles {
        val defaultVariables: String = header(VARIABLES) + System.lineSeparator() + System.lineSeparator()
        val defaultOutputs: String = header(OUTPUTS) + System.lineSeparator() + System.lineSeparator()
        var variables: String? = defaultVariables
        var outputs: String? = defaultOutputs
        var main: String = header(MAIN) + System.lineSeparator() + System.lineSeparator()
        configuration?.let {
            main += it.toString() + System.lineSeparator()
        } ?: throw IllegalStateException("Terraform configuration in ${name()} not set!")
        for (child in children) {
            when (child) {
                is OutputVariable -> outputs += child.toString() + System.lineSeparator()
                is InputVariable -> variables += child.toString() + System.lineSeparator()
                else -> main += child.toString() + System.lineSeparator()
            }
        }
        if (variables == defaultVariables) {
            variables = null
        }
        if (outputs == defaultOutputs) {
            outputs = null
        }
        return ModuleFiles(
            main.removeSuffix(System.lineSeparator()),
            variables?.removeSuffix(System.lineSeparator()),
            outputs?.removeSuffix(System.lineSeparator())
        )
    }

    open fun debugFiles(): T = apply {
        val file: ModuleFiles = toFileStrings()
        logger.debug(
            file.main + (file.outputs ?: "") + (file.variables ?: "")
        )
    } as T

    private fun header(file: String): String =
        OneLineComment(OneLineSymbol.DOUBLE_SLASH, "${name()} $file").toString()

    abstract fun name(): String
}
