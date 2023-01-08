package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudNetworkRoute {
    private const val HCLOUD_NETWORK_ROUTE: String = "hcloud_network_route"
    private const val NETWORK_ID: String = "network_id"
    private const val DESTINATION: String = "destination"
    private const val GATEWAY: String = "gateway"
    private const val ID: String = "id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val networkId get() = Reference<TfNumber>(referenceString(NETWORK_ID))
        val destination get() = Reference<TfString>(referenceString(DESTINATION))
        val gateway get() = Reference<TfString>(referenceString(GATEWAY))

        class Builder: GBuilder<Builder>() {
            private val networkIdBuilder: Argument.Builder = Argument.Builder().name(NETWORK_ID)
            private val destinationBuilder: Argument.Builder = Argument.Builder().name(DESTINATION)
            private val gatewayBuilder: Argument.Builder = Argument.Builder().name(GATEWAY)
            init { resourceType(HCLOUD_NETWORK_ROUTE) }

            fun networkId(networkId: Int) = apply { networkIdBuilder.value(networkId.toDouble()) }
            fun networkId(ref: Reference<TfNumber>) = apply { networkIdBuilder.raw(ref.toString()) }
            fun destination(destination: String) = apply { destinationBuilder.value(destination) }
            fun destination(ref: Reference<TfString>) = apply { destinationBuilder.raw(ref.toString()) }
            fun gateway(gateway: String) = apply { gatewayBuilder.value(gateway) }
            fun gateway(ref: Reference<TfString>) = apply { gatewayBuilder.raw(ref.toString()) }
            override fun build(): Resource {
                addElement(networkIdBuilder.build())
                addElement(destinationBuilder.build())
                addElement(gatewayBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
