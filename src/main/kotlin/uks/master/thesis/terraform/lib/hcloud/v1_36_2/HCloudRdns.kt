package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
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
        val id get() = TfRef<TfNumber>(reference(ID))
        val dnsPtr get() = TfRef<TfString>(reference(DNS_PTR))
        val ipAddress get() = TfRef<TfString>(reference(IP_ADDRESS))
        val serverId get() = TfRef<TfNumber>(reference(SERVER_ID))
        val floatingIpId get() = TfRef<TfNumber>(reference(FLOATING_IP_ID))
        val loadBalancerId get() = TfRef<TfNumber>(reference(LOAD_BALANCER_ID))

        class Builder: GBuilder<Builder>() {
            private val dnsPtrBuilder: Argument.Builder = Argument.Builder().name(DNS_PTR)
            private val ipAddressBuilder: Argument.Builder = Argument.Builder().name(IP_ADDRESS)
            private val serverIdOrFloatingIpIdOrLoadBalancerIdBuilder: Argument.Builder = Argument.Builder()
            init { resourceType(HCLOUD_RDNS) }

            fun dnsPtr(dnsPtr: String) = apply { dnsPtrBuilder.value(dnsPtr) }
            fun dnsPtr(ref: TfRef<TfString>) = apply { dnsPtrBuilder.raw(ref.toString()) }
            fun ipAddress(ipAddress: String) = apply { ipAddressBuilder.value(ipAddress) }
            fun ipAddress(ref: TfRef<TfString>) = apply { ipAddressBuilder.raw(ref.toString()) }
            fun serverId(serverId: String) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(SERVER_ID).value(serverId) }
            fun serverId(ref: TfRef<TfString>) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(SERVER_ID).raw(ref.toString()) }
            fun floatingIpId(floatingIpId: String) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(FLOATING_IP_ID).value(floatingIpId) }
            fun floatingIpId(ref: TfRef<TfString>) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(FLOATING_IP_ID).raw(ref.toString()) }
            fun loadBalancerId(loadBalancerId: String) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(LOAD_BALANCER_ID).value(loadBalancerId) }
            fun loadBalancerId(ref: TfRef<TfString>) = apply { serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.name(LOAD_BALANCER_ID).raw(ref.toString()) }
            override fun build(): Resource {
                blockBuilder.addElement(dnsPtrBuilder.build()).addElement(ipAddressBuilder.build())
                    .addElement(serverIdOrFloatingIpIdOrLoadBalancerIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
