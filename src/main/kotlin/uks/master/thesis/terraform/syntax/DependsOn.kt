package uks.master.thesis.terraform.syntax

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.Reference

abstract class DependsOn {
    private companion object {
        const val DEPENDS_ON: String = "depends_on"
    }

    protected fun buildDependencies(blockBuilder: Block.Builder, dependencies: List<Reference<Raw>>) {
        if (dependencies.isNotEmpty()) {
            val dependsOnBuilder: Argument.Builder = Argument.Builder().name(DEPENDS_ON)
            dependsOnBuilder.value(dependencies.let {
                val tfListBuilder: TfList.Builder = TfList.Builder()
                it.forEach { d -> tfListBuilder.add(d) }
                tfListBuilder.build()
            })
            blockBuilder.addElement(dependsOnBuilder.build())
        }
    }
}
