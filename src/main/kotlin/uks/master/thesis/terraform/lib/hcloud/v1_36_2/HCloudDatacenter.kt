package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudDatacenter {
    private const val HCLOUD_DATACENTER: String = "hcloud_datacenter"
    private const val ID: String = "id"
    private const val NAME: String = "name"
    private const val DESCRIPTION: String = "description"
    private const val LOCATION: String = "location"
    private const val SUPPORTED_SERVER_TYPE_IDS = "supported_server_type_ids"
    private const val AVAILABLE_SERVER_TYPE_IDS = "available_server_type_ids"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val name get() = Reference<TfString>(referenceString(NAME))
        val description get() = Reference<TfString>(referenceString(DESCRIPTION))
        val location get() = Reference<TfMap>(referenceString(LOCATION))
        val supportedServerTypeIds get() = Reference<TfList>(referenceString(SUPPORTED_SERVER_TYPE_IDS))
        val availableServerTypeIds get() = Reference<TfList>(referenceString(AVAILABLE_SERVER_TYPE_IDS))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            init { dataSource(HCLOUD_DATACENTER) }

            fun id(id: Int) = apply { addElement(idOrNameBuilder.name(ID).value(id.toDouble()).build()) }
            fun id(ref: Reference<TfNumber>) = apply { addElement(idOrNameBuilder.name(ID).raw(ref.toString()).build()) }
            fun name(name: String) = apply { addElement(idOrNameBuilder.name(NAME).value(name).build()) }
            fun name(ref: Reference<TfString>) = apply { addElement(idOrNameBuilder.name(NAME).raw(ref.toString()).build()) }
            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
