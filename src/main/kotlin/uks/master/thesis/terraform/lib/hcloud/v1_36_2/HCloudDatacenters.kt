package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfRef

object HCloudDatacenters {
    private const val HCLOUD_DATACENTERS: String = "hcloud_datacenters"
    private const val DATACENTER_IDS: String = "datacenter_ids"
    private const val NAMES: String = "names"
    private const val DESCRIPTIONS: String = "descriptions"
    private const val DATACENTERS: String = "datacenters"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val datacenterIds get() = TfRef<TfList>(reference(DATACENTER_IDS))
        val names get() = TfRef<TfList>(reference(NAMES))
        val descriptions get() = TfRef<TfList>(reference(DESCRIPTIONS))
        val datacenters get() = TfRef<TfList>(reference(DATACENTERS))

        class Builder: GBuilder<Builder>() {
            init { dataSource(HCLOUD_DATACENTERS) }

            override fun build(): DataSource = DataSource(buildBlock(), buildSelf())
        }
    }
}
