package uks.master.thesis.terraform

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.elements.blocks.OutputValue
import uks.master.thesis.terraform.syntax.elements.blocks.TfModule
import uks.master.thesis.terraform.syntax.expressions.Reference

class SubModule(tfModule: TfModule): ParentModule<SubModule>(), Child {
    private var tfModule: TfModule
    override val name: String get() = tfModule.reference.toString()

    init {
        require(tfModule.type == TfModule.Type.LOCAL) {
            "Found \"${tfModule.type}\". Only modules from type LOCAL are allowed to be SubModules."
        }
        this.tfModule = tfModule
    }

    fun <S: Expression>output(outputValue: OutputValue<S>): Reference<S> = Reference("${tfModule.reference}.${outputValue.name}")

    override fun toString(): String = tfModule.toString()
}
