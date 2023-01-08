package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudFirewalls {
    private const val HCLOUD_FIREWALLS: String = "hcloud_firewalls"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val MOST_RECENT: String = "most_recent"
    private const val FIREWALLS: String = "firewalls"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val firewalls get() = Reference<TfList>(referenceString(FIREWALLS))

        class Builder: GBuilder<Builder>() {
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val mostRecentBuilder: Argument.Builder = Argument.Builder().name(MOST_RECENT)
            init { dataSource(HCLOUD_FIREWALLS) }

            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun mostRecent(mostRecent: Boolean) = apply { addElement(mostRecentBuilder.value(mostRecent).build()) }
            fun mostRecent(ref: Reference<TfBool>) = apply { addElement(mostRecentBuilder.raw(ref.toString()).build()) }
            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
