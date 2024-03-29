package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.Reference

object HCloudLocations {
    private const val HCLOUD_LOCATIONS: String = "hcloud_locations"
    private const val LOCATION_IDS: String = "location_ids"
    private const val NAMES: String = "names"
    private const val DESCRIPTIONS: String = "descriptions"
    private const val LOCATIONS: String = "locations"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val locationIds get() = Reference<TfList>(referenceString(LOCATION_IDS))
        val names get() = Reference<TfList>(referenceString(NAMES))
        val descriptions get() = Reference<TfList>(referenceString(DESCRIPTIONS))
        val locations get() = Reference<TfList>(referenceString(LOCATIONS))

        class Builder: GBuilder<Builder>() {
            init { dataSource(HCLOUD_LOCATIONS) }

            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
