package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudNetwork {
    private const val HCLOUD_NETWORK: String = "hcloud_network"
    private const val NAME: String = "name"
    private const val IP_RANGE: String = "ip_range"
    private const val LABELS: String = "labels"
    private const val DELETE_PROTECTION: String = "delete_protection"
    private const val ID: String = "id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val ipRange get() = TfRef<TfString>(reference(IP_RANGE))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val ipRangeBuilder: Argument.Builder = Argument.Builder().name(IP_RANGE)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private val deleteProtectionBuilder: Argument.Builder = Argument.Builder().name(DELETE_PROTECTION)
            init { resourceType(HCLOUD_NETWORK) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: TfRef<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun ipRange(ipRange: String) = apply { ipRangeBuilder.value(ipRange) }
            fun ipRange(ref: TfRef<TfString>) = apply { ipRangeBuilder.raw(ref.toString()) }
            fun labels(labels: TfMap) = apply { blockBuilder.addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { blockBuilder.addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { blockBuilder.addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: TfRef<TfBool>) = apply { blockBuilder.addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                blockBuilder.addElement(nameBuilder.build()).addElement(ipRangeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
