package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
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
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val description get() = TfRef<TfString>(reference(DESCRIPTION))
        val location get() = TfRef<TfMap>(reference(LOCATION))
        val supportedServerTypeIds get() = TfRef<TfList>(reference(SUPPORTED_SERVER_TYPE_IDS))
        val availableServerTypeIds get() = TfRef<TfList>(reference(AVAILABLE_SERVER_TYPE_IDS))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            init { dataSource(HCLOUD_DATACENTER) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: TfRef<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: TfRef<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            override fun build(): DataSource {
                addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
