package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudNetworks {
    private const val HCLOUD_NETWORKS: String = "hcloud_networks"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val NETWORKS: String = "networks"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val networks get() = Reference<TfList>(referenceString(NETWORKS))

        class Builder: GBuilder<Builder>() {
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_NETWORKS) }

            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
