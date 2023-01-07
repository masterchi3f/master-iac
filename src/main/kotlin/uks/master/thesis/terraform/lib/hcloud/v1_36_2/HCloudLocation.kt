package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudLocation {
    private const val HCLOUD_LOCATION: String = "hcloud_location"
    private const val ID: String = "id"
    private const val NAME: String = "name"
    private const val DESCRIPTION: String = "description"
    private const val CITY: String = "city"
    private const val COUNTRY: String = "country"
    private const val LATITUDE: String = "latitude"
    private const val LONGITUDE: String = "longitude"
    private const val NETWORK_ZONE: String = "network_zone"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(referenceString(ID))
        val name get() = TfRef<TfString>(referenceString(NAME))
        val description get() = TfRef<TfString>(referenceString(DESCRIPTION))
        val city get() = TfRef<TfString>(referenceString(CITY))
        val country get() = TfRef<TfString>(referenceString(COUNTRY))
        val latitude get() = TfRef<TfNumber>(referenceString(LATITUDE))
        val longitude get() = TfRef<TfNumber>(referenceString(LONGITUDE))
        val networkZone get() = TfRef<TfString>(referenceString(NETWORK_ZONE))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            init { dataSource(HCLOUD_LOCATION) }

            fun id(id: Int) = apply { addElement(idOrNameBuilder.name(ID).value(id.toDouble()).build()) }
            fun id(ref: TfRef<TfNumber>) = apply { addElement(idOrNameBuilder.name(ID).raw(ref.toString()).build()) }
            fun name(name: String) = apply { addElement(idOrNameBuilder.name(NAME).value(name).build()) }
            fun name(ref: TfRef<TfString>) = apply { addElement(idOrNameBuilder.name(NAME).raw(ref.toString()).build()) }
            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
