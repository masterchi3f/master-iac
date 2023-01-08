package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudServerNetwork {
    private const val HCLOUD_SERVER_NETWORK: String = "hcloud_server_network"
    private const val SERVER_ID: String = "server_id"
    private const val ALIAS_IPS: String = "alias_ips"
    private const val NETWORK_ID: String = "network_id"
    private const val SUBNET_ID: String = "subnet_id"
    private const val IP: String = "ip"
    private const val ID: String = "id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val serverId get() = Reference<TfNumber>(referenceString(SERVER_ID))
        val networkId get() = Reference<TfNumber>(referenceString(NETWORK_ID))
        val ip get() = Reference<TfString>(referenceString(IP))
        val aliasIps get() = Reference<TfList>(referenceString(ALIAS_IPS))

        class Builder: GBuilder<Builder>() {
            private val serverIdBuilder: Argument.Builder = Argument.Builder().name(SERVER_ID)
            private val aliasIpsBuilder: Argument.Builder = Argument.Builder().name(ALIAS_IPS)
            private val networkIdBuilder: Argument.Builder = Argument.Builder().name(NETWORK_ID)
            private val subnetIdBuilder: Argument.Builder = Argument.Builder().name(SUBNET_ID)
            private val ipBuilder: Argument.Builder = Argument.Builder().name(IP)
            private var hasNetworkIdOrSubnetId = false
            init { resourceType(HCLOUD_SERVER_NETWORK) }

            fun serverId(serverId: Int) = apply { serverIdBuilder.value(serverId.toDouble()) }
            fun serverId(ref: Reference<TfNumber>) = apply { serverIdBuilder.raw(ref.toString()) }
            fun aliasIps(aliasIps: TfList) = apply { addElement(aliasIpsBuilder.value(aliasIps).build()) }
            fun aliasIps(ref: Reference<TfList>) = apply { addElement(aliasIpsBuilder.raw(ref.toString()).build()) }
            fun networkId(networkId: Int) = apply { hasNetworkIdOrSubnetId = true; addElement(networkIdBuilder.value(networkId.toDouble()).build()) }
            fun networkId(ref: Reference<TfNumber>) = apply { hasNetworkIdOrSubnetId = true; addElement(networkIdBuilder.raw(ref.toString()).build()) }
            fun subnetId(subnetId: Int) = apply { hasNetworkIdOrSubnetId = true; addElement(subnetIdBuilder.value(subnetId.toDouble()).build()) }
            fun subnetId(ref: Reference<TfNumber>) = apply { hasNetworkIdOrSubnetId = true; addElement(subnetIdBuilder.raw(ref.toString()).build()) }
            fun ip(ip: String) = apply { addElement(ipBuilder.value(ip).build()) }
            fun ip(ref: Reference<TfString>) = apply { addElement(ipBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                if (!hasNetworkIdOrSubnetId) {
                    throw IllegalArgumentException("networkId or subnetId must be set")
                }
                addElement(serverIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
