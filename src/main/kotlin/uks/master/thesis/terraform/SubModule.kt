package uks.master.thesis.terraform

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.elements.blocks.OutputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.TfModule
import uks.master.thesis.terraform.syntax.expressions.TfRef

class SubModule(tfModule: TfModule): ParentModule<SubModule>(), Child {
    private var tfModule: TfModule
    override val name: String get() = tfModule.self

    init {
        if (tfModule.type == TfModule.Type.LOCAL) {
            this.tfModule = tfModule
        } else {
            throw IllegalArgumentException(
                "Found \"${tfModule.type}\". Only modules from type LOCAL are allowed to be SubModules."
            )
        }
    }

    fun <S: Expression>output(outputVariable: OutputVariable<S>): TfRef<S> = TfRef("${tfModule.self}.${outputVariable.name}")

    override fun toString(): String = tfModule.toString()
}
