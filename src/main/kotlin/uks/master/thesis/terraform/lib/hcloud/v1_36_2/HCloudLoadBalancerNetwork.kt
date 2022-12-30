package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudLoadBalancerNetwork {
    private const val HCLOUD_LOAD_BALANCER_NETWORK: String = "hcloud_load_balancer_network"
    private const val NETWORK_ID: String = "network_id"
    private const val SUBNET_ID: String = "subnet_id"
    private const val IP: String = "ip"
    private const val ENABLE_PUBLIC_INTERFACE: String = "enable_public_interface"
    private const val ID: String = "id"
    private const val LOAD_BALANCER_ID: String = "load_balancer_id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val networkId get() = TfRef<TfNumber>(reference(NETWORK_ID))
        val loadBalancerId get() = TfRef<TfNumber>(reference(LOAD_BALANCER_ID))
        val ip get() = TfRef<TfString>(reference(IP))

        class Builder: GBuilder<Builder>() {
            private val loadBalancerIdBuilder: Argument.Builder = Argument.Builder().name(LOAD_BALANCER_ID)
            private val networkIdBuilder: Argument.Builder = Argument.Builder().name(NETWORK_ID)
            private val subnetIdBuilder: Argument.Builder = Argument.Builder().name(SUBNET_ID)
            private val ipBuilder: Argument.Builder = Argument.Builder().name(IP)
            private val enablePublicInterfaceBuilder: Argument.Builder = Argument.Builder().name(ENABLE_PUBLIC_INTERFACE)
            private var networkIdOrSubnetIdSet = false
            init { resourceType(HCLOUD_LOAD_BALANCER_NETWORK) }

            fun loadBalancerId(loadBalancerId: Int) = apply { loadBalancerIdBuilder.value(loadBalancerId.toDouble()) }
            fun loadBalancerId(ref: TfRef<TfNumber>) = apply { loadBalancerIdBuilder.raw(ref.toString()) }
            fun networkId(networkId: Int) = apply { networkIdOrSubnetIdSet = true; addElement(networkIdBuilder.value(networkId.toDouble()).build()) }
            fun networkId(ref: TfRef<TfNumber>) = apply { networkIdOrSubnetIdSet = true; addElement(networkIdBuilder.raw(ref.toString()).build()) }
            fun subnetId(subnetId: Int) = apply { networkIdOrSubnetIdSet = true; addElement(subnetIdBuilder.value(subnetId.toDouble()).build()) }
            fun subnetId(ref: TfRef<TfNumber>) = apply { networkIdOrSubnetIdSet = true; addElement(subnetIdBuilder.raw(ref.toString()).build()) }
            fun ip(ip: String) = apply { addElement(ipBuilder.value(ip).build()) }
            fun ip(ref: TfRef<TfString>) = apply { addElement(ipBuilder.raw(ref.toString()).build()) }
            fun enablePublicInterface(enablePublicInterface: Boolean) = apply { addElement(enablePublicInterfaceBuilder.value(enablePublicInterface).build()) }
            fun enablePublicInterface(ref: TfRef<TfBool>) = apply { addElement(enablePublicInterfaceBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                if (!networkIdOrSubnetIdSet) {
                    throw IllegalArgumentException("Either networkId or subnetId must be set!")
                }
                addElement(loadBalancerIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
