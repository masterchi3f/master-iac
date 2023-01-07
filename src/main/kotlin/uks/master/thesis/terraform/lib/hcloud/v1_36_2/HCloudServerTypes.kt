package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfRef

object HCloudServerTypes {
    private const val HCLOUD_SERVER_TYPES: String = "hcloud_server_types"
    private const val SERVER_TYPES: String = "server_types"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val serverTypes get() = TfRef<TfList>(referenceString(SERVER_TYPES))

        class Builder: GBuilder<Builder>() {
            init { dataSource(HCLOUD_SERVER_TYPES) }

            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
