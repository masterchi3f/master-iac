package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block

object HCloudSshKeys {
    private const val HCLOUD_SSH_KEYS: String = "hcloud_ssh_keys"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val SSH_KEYS: String = "ssh_keys"

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val sshKeys get() = "${reference()}.$SSH_KEYS"

        class Builder: uks.master.thesis.terraform.syntax.elements.blocks.DataSource.GBuilder<Builder>() {
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_SSH_KEYS) }

            fun withSelector(selector: String) = apply { blockBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelectorRef(ref: String) = apply { blockBuilder.addElement(withSelectorBuilder.raw(ref).build()) }
            override fun build() = DataSource(buildBlock(), buildSelf())
        }
    }
}
