package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef

object HCloudFirewallAttachment {
    private const val HCLOUD_FIREWALL_ATTACHMENT: String = "hcloud_firewall_attachment"
    private const val FIREWALL_ID: String = "firewall_id"
    private const val SERVER_IDS: String = "server_ids"
    private const val LABEL_SELECTORS: String = "label_selectors"
    private const val ID: String = "id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val firewallId get() = TfRef<TfNumber>(reference(FIREWALL_ID))
        val serverIds get() = TfRef<TfList>(reference(SERVER_IDS))
        val labelSelectors get() = TfRef<TfList>(reference(LABEL_SELECTORS))

        class Builder: GBuilder<Builder>() {
            private val firewallIdBuilder: Argument.Builder = Argument.Builder().name(FIREWALL_ID)
            private val serverIdsBuilder: Argument.Builder = Argument.Builder().name(SERVER_IDS)
            private val labelSelectorsBuilder: Argument.Builder = Argument.Builder().name(LABEL_SELECTORS)
            init { resourceType(HCLOUD_FIREWALL_ATTACHMENT) }

            fun firewallId(id: Int) = apply { firewallIdBuilder.value(id.toDouble()) }
            fun firewallId(ref: TfRef<TfNumber>) = apply { firewallIdBuilder.raw(ref.toString()) }
            fun serverIds(ids: TfList) = apply { blockBuilder.addElement(serverIdsBuilder.value(ids).build()) }
            fun serverIds(ref: TfRef<TfList>) = apply { blockBuilder.addElement(serverIdsBuilder.raw(ref.toString()).build()) }
            fun labelsSelectors(labelSelectors: TfList) = apply { blockBuilder.addElement(labelSelectorsBuilder.value(labelSelectors).build()) }
            fun labelsSelectors(ref: TfRef<TfList>) = apply { blockBuilder.addElement(labelSelectorsBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                blockBuilder.addElement(firewallIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
