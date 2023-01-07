package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudVolumes {
    private const val HCLOUD_VOLUMES: String = "hcloud_volumes"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val WITH_STATUS: String = "with_status"
    private const val VOLUMES: String = "volumes"

    enum class Status(private val status: String) {
        CREATING("creating"),
        AVAILABLE("available");
        override fun toString(): String = status
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val volumes get() = TfRef<TfNumber>(referenceString(VOLUMES))

        class Builder: GBuilder<Builder>() {
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val withStatusBuilder: Argument.Builder = Argument.Builder().name(WITH_STATUS)
            init { dataSource(HCLOUD_VOLUMES) }

            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun withStatus(withStatus: List<Status>) = apply {
                val set: Set<Status> = withStatus.toSet()
                val tfListBuilder: TfList.Builder = TfList.Builder()
                set.forEach { tfListBuilder.add(it.toString()) }
                addElement(withStatusBuilder.value(tfListBuilder.build()).build())
            }
            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
