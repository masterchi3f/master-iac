package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudCertificates {
    private const val HCLOUD_CERTIFICATES: String = "hcloud_certificates"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val CERTIFICATES: String = "certificates"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val certificates get() = TfRef<TfList>(referenceString(CERTIFICATES))

        class Builder: GBuilder<Builder>() {
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_CERTIFICATES) }

            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
