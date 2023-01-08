package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudRdns {
    private const val HCLOUD_RDNS: String = "hcloud_rdns"
    private const val DNS_PTR: String = "dns_ptr"
    private const val IP_ADDRESS: String = "ip_address"
    private const val SERVER_ID: String = "server_id"
    private const val FLOATING_IP_ID: String = "floating_ip_id"
    private const val LOAD_BALANCER_ID: String = "load_balancer_id"
    private const val ID: String = "id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val dnsPtr get() = Reference<TfString>(referenceString(DNS_PTR))
        val ipAddress get() = Reference<TfString>(referenceString(IP_ADDRESS))
        val serverId get() = Reference<TfNumber>(referenceString(SERVER_ID))
        val floatingIpId get() = Reference<TfNumber>(referenceString(FLOATING_IP_ID))
        val loadBalancerId get() = Reference<TfNumber>(referenceString(LOAD_BALANCER_ID))

        class Builder: GBuilder<Builder>() {
            private val dnsPtrBuilder: Argument.Builder = Argument.Builder().name(DNS_PTR)
            private val ipAddressBuilder: Argument.Builder = Argument.Builder().name(IP_ADDRESS)
            private val serverIdOrFloatingIpIdOrLoadBalancerIdBuilder: Argument.Builder = Argument.Builder()
            init { resourceType(HCLOUD_RDNS) }

            fun dnsPtr(dnsPtr: String) = apply { dnsPtrBuilder.value(dnsPtr) }
            fun dnsPtr(ref: Reference<TfString>) = apply { dnsPtrBuilder.raw(ref.toString()) }
            fun ipAddress(ipAddress: String) = apply { ipAddressBuilder.value(ipAddress) }
            fun ipAddress(ref: Reference<TfString>) = apply { ipAddressBuilder.raw(ref.toString()) }
            fun serverId(serverId: Int) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(SERVER_ID).value(serverId.toDouble()) }
            fun serverId(ref: Reference<TfNumber>) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(SERVER_ID).raw(ref.toString()) }
            fun floatingIpId(floatingIpId: Int) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(FLOATING_IP_ID).value(floatingIpId.toDouble()) }
            fun floatingIpId(ref: Reference<TfNumber>) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(FLOATING_IP_ID).raw(ref.toString()) }
            fun loadBalancerId(loadBalancerId: Int) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(LOAD_BALANCER_ID).value(loadBalancerId.toDouble()) }
            fun loadBalancerId(ref: Reference<TfNumber>) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(LOAD_BALANCER_ID).raw(ref.toString()) }
            override fun build(): Resource {
                addElement(dnsPtrBuilder.build())
                addElement(ipAddressBuilder.build())
                addElement(serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
