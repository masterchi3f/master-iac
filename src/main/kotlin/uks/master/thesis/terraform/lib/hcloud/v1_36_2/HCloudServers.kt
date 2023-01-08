package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudServers {
    private const val HCLOUD_SERVERS: String = "hcloud_servers"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val WITH_STATUS: String = "with_status"
    private const val SERVERS: String = "servers"

    enum class Status(private val status: String) {
        INITIALIZING("initializing"),
        STARTING("starting"),
        RUNNING("running"),
        STOPPING("stopping"),
        OFF("off"),
        DELETING("deleting"),
        REBUILDING("rebuilding"),
        MIGRATING("migrating"),
        UNKNOWN("unknown");
        override fun toString(): String = status
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val servers get() = Reference<TfList>(referenceString(SERVERS))

        class Builder: GBuilder<Builder>() {
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val withStatusBuilder: Argument.Builder = Argument.Builder().name(WITH_STATUS)
            init { dataSource(HCLOUD_SERVERS) }

            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
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
