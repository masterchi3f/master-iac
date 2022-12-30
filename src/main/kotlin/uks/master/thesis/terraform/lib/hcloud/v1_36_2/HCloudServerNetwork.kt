package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
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
        val id get() = TfRef<TfNumber>(reference(ID))
        val serverId get() = TfRef<TfNumber>(reference(SERVER_ID))
        val networkId get() = TfRef<TfNumber>(reference(NETWORK_ID))
        val ip get() = TfRef<TfString>(reference(IP))
        val aliasIps get() = TfRef<TfList>(reference(ALIAS_IPS))

        class Builder: GBuilder<Builder>() {
            private val serverIdBuilder: Argument.Builder = Argument.Builder().name(SERVER_ID)
            private val aliasIpsBuilder: Argument.Builder = Argument.Builder().name(ALIAS_IPS)
            private val networkIdBuilder: Argument.Builder = Argument.Builder().name(NETWORK_ID)
            private val subnetIdBuilder: Argument.Builder = Argument.Builder().name(SUBNET_ID)
            private val ipBuilder: Argument.Builder = Argument.Builder().name(IP)
            private var hasNetworkIdOrSubnetId = false
            init { resourceType(HCLOUD_SERVER_NETWORK) }

            fun serverId(serverId: String) = apply { serverIdBuilder.value(serverId) }
            fun serverId(ref: TfRef<TfString>) = apply { serverIdBuilder.raw(ref.toString()) }
            fun aliasIps(aliasIps: TfList) = apply { blockBuilder.addElement(aliasIpsBuilder.value(aliasIps).build()) }
            fun aliasIps(ref: TfRef<TfList>) = apply { blockBuilder.addElement(aliasIpsBuilder.raw(ref.toString()).build()) }
            fun networkId(networkId: String) = apply { hasNetworkIdOrSubnetId = true; blockBuilder.addElement(networkIdBuilder.value(networkId).build()) }
            fun networkId(ref: TfRef<TfString>) = apply { hasNetworkIdOrSubnetId = true; blockBuilder.addElement(networkIdBuilder.raw(ref.toString()).build()) }
            fun subnetId(subnetId: String) = apply { hasNetworkIdOrSubnetId = true; blockBuilder.addElement(subnetIdBuilder.value(subnetId).build()) }
            fun subnetId(ref: TfRef<TfString>) = apply { hasNetworkIdOrSubnetId = true; blockBuilder.addElement(subnetIdBuilder.raw(ref.toString()).build()) }
            fun ip(ip: String) = apply { blockBuilder.addElement(ipBuilder.value(ip).build()) }
            fun ip(ref: TfRef<TfString>) = apply { blockBuilder.addElement(ipBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                if (!hasNetworkIdOrSubnetId) {
                    throw IllegalArgumentException("networkId or subnetId must be set")
                }
                blockBuilder.addElement(serverIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
