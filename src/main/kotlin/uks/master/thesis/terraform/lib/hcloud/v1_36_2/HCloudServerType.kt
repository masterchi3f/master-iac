package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudServerType {
    private const val HCLOUD_SERVER_TYPE: String = "hcloud_server_type"
    private const val ID: String = "id"
    private const val NAME: String = "name"
    private const val DESCRIPTION: String = "description"
    private const val CORES: String = "cores"
    private const val MEMORY: String = "memory"
    private const val DISK: String = "disk"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val name get() = Reference<TfString>(referenceString(NAME))
        val description get() = Reference<TfString>(referenceString(DESCRIPTION))
        val cores get() = Reference<TfNumber>(referenceString(CORES))
        val memory get() = Reference<TfNumber>(referenceString(MEMORY))
        val disk get() = Reference<TfNumber>(referenceString(DISK))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            init { dataSource(HCLOUD_SERVER_TYPE) }

            fun id(id: Int) = apply { addElement(idOrNameBuilder.name(ID).value(id.toDouble()).build()) }
            fun id(ref: Reference<TfNumber>) = apply { addElement(idOrNameBuilder.name(ID).raw(ref.toString()).build()) }
            fun name(name: String) = apply { addElement(idOrNameBuilder.name(NAME).value(name).build()) }
            fun name(ref: Reference<TfString>) = apply { addElement(idOrNameBuilder.name(NAME).raw(ref.toString()).build()) }
            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
