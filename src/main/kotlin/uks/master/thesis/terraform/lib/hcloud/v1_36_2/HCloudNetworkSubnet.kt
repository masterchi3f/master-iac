package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudNetworkSubnet {
    private const val HCLOUD_NETWORK_SUBNET: String = "hcloud_network_subnet"
    private const val NETWORK_ID: String = "network_id"
    private const val TYPE: String = "type"
    private const val IP_RANGE: String = "ip_range"
    private const val NETWORK_ZONE: String = "network_zone"
    private const val VSWITCH_ID: String = "vswitch_id"
    private const val ID: String = "id"

    enum class Type(private val type: String) {
        SERVER("server"),
        CLOUD("cloud"),
        VSWITCH("vswitch");
        override fun toString(): String = type
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val networkId get() = TfRef<TfNumber>(reference(NETWORK_ID))
        val type get() = TfRef<TfString>(reference(TYPE))
        val ipRange get() = TfRef<TfString>(reference(IP_RANGE))
        val networkZone get() = TfRef<TfString>(reference(NETWORK_ZONE))
        val vswitchId get() = TfRef<TfNumber>(reference(VSWITCH_ID))

        class Builder: GBuilder<Builder>() {
            private val networkIdBuilder: Argument.Builder = Argument.Builder().name(NETWORK_ID)
            private val typeBuilder: Argument.Builder = Argument.Builder().name(TYPE)
            private val ipRangeBuilder: Argument.Builder = Argument.Builder().name(IP_RANGE)
            private val networkZoneBuilder: Argument.Builder = Argument.Builder().name(NETWORK_ZONE)
            private val vswitchIdBuilder: Argument.Builder = Argument.Builder().name(VSWITCH_ID)
            private lateinit var _type: Type
            private var hasVswitchId = false
            init { resourceType(HCLOUD_NETWORK_SUBNET) }

            fun networkId(networkId: Int) = apply { networkIdBuilder.value(networkId.toDouble()) }
            fun networkId(ref: TfRef<TfNumber>) = apply { networkIdBuilder.raw(ref.toString()) }
            fun type(type: Type) = apply { _type = type; typeBuilder.value(type.toString()) }
            fun ipRange(ipRange: String) = apply { ipRangeBuilder.value(ipRange) }
            fun ipRange(ref: TfRef<TfString>) = apply { ipRangeBuilder.raw(ref.toString()) }
            fun networkZone(networkZone: String) = apply { networkZoneBuilder.value(networkZone) }
            fun networkZone(ref: TfRef<TfString>) = apply { networkZoneBuilder.raw(ref.toString()) }
            fun vswitchId(vswitchId: Int) = apply { hasVswitchId = true; vswitchIdBuilder.value(vswitchId.toDouble()) }
            fun vswitchId(ref: TfRef<TfNumber>) = apply { hasVswitchId = true; vswitchIdBuilder.raw(ref.toString()) }
            override fun build(): Resource {
                blockBuilder
                    .addElement(networkIdBuilder.build())
                    .addElement(typeBuilder.build())
                    .addElement(ipRangeBuilder.build())
                    .addElement(networkZoneBuilder.build())
                if (_type == Type.VSWITCH) {
                    blockBuilder.addElement(vswitchIdBuilder.build())
                } else if (hasVswitchId) {
                    throw IllegalArgumentException("vswitchId was set but subnet type is $_type")
                }
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
