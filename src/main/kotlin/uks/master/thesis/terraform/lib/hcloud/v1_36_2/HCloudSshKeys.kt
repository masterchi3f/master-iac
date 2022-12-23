package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.blocks.DataSource

object HCloudSshKeys {
    private const val HCLOUD_SSH_KEYS: String = "hcloud_ssh_keys"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val SSH_KEYS: String = "ssh_keys"

    class _DataSource private constructor(private val data: DataSource): Element, Child {
        val sshKeys get() = "${data.reference()}.$SSH_KEYS"

        class Builder {
            private val datasourceBuilder: DataSource.Builder = DataSource.Builder()
            private lateinit var _dataName: Identifier
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)

            fun dataName(dataName: String) = apply { preventDupDataName(); _dataName = Identifier(dataName) }
            fun withSelector(selector: String) = apply { datasourceBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelectorRef(ref: String) = apply { datasourceBuilder.addElement(withSelectorBuilder.raw(ref).build()) }
            fun build() = _DataSource(
                datasourceBuilder.source(HCLOUD_SSH_KEYS).name(_dataName.toString()).build()
            )

            private fun preventDupDataName() {
                if (::_dataName.isInitialized) throw IllegalArgumentException("Data source name was already set to $_dataName!")
            }
        }

        override fun toString(): String = data.toString()
    }
}
