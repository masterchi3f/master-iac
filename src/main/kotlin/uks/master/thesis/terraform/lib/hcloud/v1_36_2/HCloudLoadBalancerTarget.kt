package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudLoadBalancerTarget {
    private const val HCLOUD_LOAD_BALANCER_TARGET: String = "hcloud_load_balancer_target"
    private const val TYPE: String = "type"
    private const val LOAD_BALANCER_ID: String = "load_balancer_id"
    private const val SERVER_ID: String = "server_id"
    private const val LABEL_SELECTOR: String = "label_selector"
    private const val IP: String = "ip"
    private const val USE_PRIVATE_IP: String = "use_private_ip"

    enum class Type(private val type: String) {
        SERVER("server"),
        LABEL_SELECTOR("label_selector"),
        IP("ip");
        override fun toString(): String = type
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val type get() = Reference<TfString>(referenceString(TYPE))
        val serverId get() = Reference<TfNumber>(referenceString(SERVER_ID))
        val labelSelector get() = Reference<TfString>(referenceString(LABEL_SELECTOR))
        val ip get() = Reference<TfString>(referenceString(IP))
        val usePrivateIp get() = Reference<TfBool>(referenceString(USE_PRIVATE_IP))

        class Builder: GBuilder<Builder>() {
            private val typeBuilder: Argument.Builder = Argument.Builder().name(TYPE)
            private val loadBalancerIdBuilder: Argument.Builder = Argument.Builder().name(LOAD_BALANCER_ID)
            private val valueBuilder: Argument.Builder = Argument.Builder()
            private val usePrivateIpBuilder: Argument.Builder = Argument.Builder().name(USE_PRIVATE_IP)
            private lateinit var _type: Type
            private var usePrivateIpIsSet = false
            init { resourceType(HCLOUD_LOAD_BALANCER_TARGET) }

            fun type(type: Type) = apply { _type = type; typeBuilder.value(type.toString()) }
            fun loadBalancerId(loadBalancerId: Int) = apply { loadBalancerIdBuilder.value(loadBalancerId.toDouble()) }
            fun loadBalancerId(ref: Reference<TfNumber>) = apply { loadBalancerIdBuilder.raw(ref.toString()) }
            fun serverId(serverId: Int) = apply { valueBuilder.name(SERVER_ID).value(serverId.toDouble()) }
            fun serverId(ref: Reference<TfNumber>) = apply { valueBuilder.name(SERVER_ID).raw(ref.toString()) }
            fun labelSelector(labelSelector: String) = apply { valueBuilder.name(LABEL_SELECTOR).value(labelSelector) }
            fun labelSelector(ref: Reference<TfString>) = apply { valueBuilder.name(LABEL_SELECTOR).raw(ref.toString()) }
            fun ip(ip: String) = apply { valueBuilder.name(IP).value(ip) }
            fun ip(ref: Reference<TfString>) = apply { valueBuilder.name(IP).raw(ref.toString()) }
            fun usePrivateIp(usePrivateIp: Boolean) = apply { usePrivateIpIsSet = true; usePrivateIpBuilder.value(usePrivateIp) }
            fun usePrivateIp(ref: Reference<TfBool>) = apply { usePrivateIpIsSet = true; usePrivateIpBuilder.raw(ref.toString()) }
            override fun build(): Resource {
                addElement(typeBuilder.build())
                addElement(loadBalancerIdBuilder.build())
                addElement(valueBuilder.build())
                if (usePrivateIpIsSet) {
                    require(_type == Type.IP) { "usePrivateIp is only allowed with type ${Type.LABEL_SELECTOR} and ${Type.SERVER}"}
                    addElement(usePrivateIpBuilder.build())
                }
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
