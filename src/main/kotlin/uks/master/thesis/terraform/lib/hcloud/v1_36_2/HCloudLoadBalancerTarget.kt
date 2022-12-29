package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString
import uks.master.thesis.terraform.utils.Utils

object HCloudLoadBalancerTarget {
    private const val HCLOUD_LOAD_BALANCER_TARGET: String = "hcloud_load_balancer_target"
    private const val TYPE: String = "type"
    private const val LOAD_BALANCER_ID: String = "load_balancer_id"
    private const val SERVER_ID: String = "server_id"
    private const val LABEL_SELECTOR: String = "label_selector"
    private const val IP: String = "ip"
    private const val USE_PRIVATE_IP: String = "use_private_ip"

    class ExportedAttributes(private val referencePrefix: String) {
        fun reference(index: Int? = null): String = "$referencePrefix[${Utils.index(index)}]".removeSuffix("[*]")
        fun type(index: Int? = null): String = "$referencePrefix[${Utils.index(index)}].$TYPE"
        fun serverId(index: Int? = null): String = "$referencePrefix[${Utils.index(index)}].$SERVER_ID"
        fun labelSelector(index: Int? = null): String = "$referencePrefix[${Utils.index(index)}].$LABEL_SELECTOR"
        fun ip(index: Int? = null): String = "$referencePrefix[${Utils.index(index)}].$IP"
        fun usePrivateIp(index: Int? = null): String = "$referencePrefix[${Utils.index(index)}].$USE_PRIVATE_IP"
    }

    enum class Type(private val type: String) {
        SERVER("server"),
        LABEL_SELECTOR("label_selector"),
        IP("ip");
        override fun toString(): String = type
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val type get() = TfRef<TfString>(reference(TYPE))
        val serverId get() = TfRef<TfNumber>(reference(SERVER_ID))
        val labelSelector get() = TfRef<TfString>(reference(LABEL_SELECTOR))
        val ip get() = TfRef<TfString>(reference(IP))
        val usePrivateIp get() = TfRef<TfBool>(reference(USE_PRIVATE_IP))

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
            fun loadBalancerId(ref: TfRef<TfNumber>) = apply { loadBalancerIdBuilder.raw(ref.toString()) }
            fun serverId(serverId: Int) = apply { valueBuilder.value(serverId.toDouble()) }
            fun serverId(ref: TfRef<TfNumber>) = apply { valueBuilder.raw(ref.toString()) }
            fun labelSelector(labelSelector: String) = apply { valueBuilder.value(labelSelector) }
            fun labelSelector(ref: TfRef<TfString>) = apply { valueBuilder.raw(ref.toString()) }
            fun ip(ip: String) = apply { valueBuilder.value(ip) }
            fun ip(ref: TfRef<TfString>) = apply { valueBuilder.raw(ref.toString()) }
            fun usePrivateIp(usePrivateIp: Boolean) = apply { usePrivateIpIsSet = true; usePrivateIpBuilder.value(usePrivateIp) }
            fun usePrivateIp(ref: TfRef<TfBool>) = apply { usePrivateIpIsSet = true; usePrivateIpBuilder.raw(ref.toString()) }
            override fun build(): Resource {
                blockBuilder.addElement(typeBuilder.build()).addElement(loadBalancerIdBuilder.build()).addElement(valueBuilder.build())
                if (usePrivateIpIsSet) {
                    if (_type != Type.IP) {
                        blockBuilder.addElement(usePrivateIpBuilder.build())
                    } else {
                        throw IllegalArgumentException("usePrivateIp is only allowed with type ${Type.LABEL_SELECTOR} and ${Type.SERVER}")
                    }
                }
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
