package uks.master.thesis.terraform

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.elements.OneLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineSymbol
import uks.master.thesis.terraform.syntax.elements.blocks.OutputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.Provider
import uks.master.thesis.terraform.syntax.elements.blocks.Terraform
import uks.master.thesis.terraform.syntax.elements.blocks.TfModule

abstract class ParentModule {
    private var configuration: Terraform? = null
    var children: List<Child> = mutableListOf()
        private set

    fun setConfiguration(terraform: Terraform) = apply {
        configuration = terraform
    }

    fun addChild(child: Child) = apply {
        if (child is TfModule && child.type == TfModule.Type.LOCAL) {
            throw IllegalArgumentException(
                "Found TfModule from type LOCAL. Wrap local modules to be SubModules."
            )
        }
        children = children + child
    }

    open fun toFile(): String {
        var result = OneLineComment(OneLineSymbol.DOUBLE_SLASH, name()).toString()
        result += System.lineSeparator() + System.lineSeparator()
        configuration?.let {
            result += it.toString() + System.lineSeparator()
        } ?: throw IllegalStateException("Terraform configuration in ${name()} not set!")
        children.forEach {
            result += it.toString() + System.lineSeparator()
        }
        return result
    }

    abstract fun name(): String
}

object RootModule: ParentModule() {
    var providers: List<Provider> = mutableListOf()
        private set

    fun addProvider(provider: Provider) = apply { providers = providers + provider }

    override fun toFile(): String {
        var result = super.toFile()
        providers.forEach {
            result += it.toString() + System.lineSeparator()
        }
        return result
    }

    override fun name(): String = "module.ROOT"
}

class SubModule(tfModule: TfModule): ParentModule(), Child {
    private var tfModule: TfModule

    init {
        if (tfModule.type == TfModule.Type.LOCAL) {
            this.tfModule = tfModule
        } else {
            throw IllegalArgumentException(
                "Found \"${tfModule.type}\". Only modules from type LOCAL are allowed to be SubModules."
            )
        }
    }

    fun output(outputVariable: OutputVariable): String = "${tfModule.self}.${outputVariable.name()}"

    override fun name(): String = tfModule.self

    override fun toString(): String = tfModule.toString()
}
