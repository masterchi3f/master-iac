package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudNetwork {
    private const val HCLOUD_NETWORK: String = "hcloud_network"
    private const val NAME: String = "name"
    private const val IP_RANGE: String = "ip_range"
    private const val LABELS: String = "labels"
    private const val DELETE_PROTECTION: String = "delete_protection"
    private const val ID: String = "id"
    private const val WITH_SELECTOR: String = "with_selector"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val name get() = Reference<TfString>(referenceString(NAME))
        val ipRange get() = Reference<TfString>(referenceString(IP_RANGE))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val deleteProtection get() = Reference<TfBool>(referenceString(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val ipRangeBuilder: Argument.Builder = Argument.Builder().name(IP_RANGE)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private val deleteProtectionBuilder: Argument.Builder = Argument.Builder().name(DELETE_PROTECTION)
            init { resourceType(HCLOUD_NETWORK) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: Reference<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun ipRange(ipRange: String) = apply { ipRangeBuilder.value(ipRange) }
            fun ipRange(ref: Reference<TfString>) = apply { ipRangeBuilder.raw(ref.toString()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: Reference<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: Reference<TfBool>) = apply { addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(nameBuilder.build())
                addElement(ipRangeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val name get() = Reference<TfString>(referenceString(NAME))
        val ipRange get() = Reference<TfString>(referenceString(IP_RANGE))
        val deleteProtection get() = Reference<TfBool>(referenceString(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_NETWORK) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: Reference<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: Reference<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
