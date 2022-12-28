package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString
import uks.master.thesis.terraform.utils.Utils

object HCloudLoadBalancer {
    private const val HCLOUD_LOAD_BALANCER: String = "hcloud_load_balancer"
    private const val NAME: String = "name"
    private const val LOAD_BALANCER_TYPE: String = "load_balancer_type"
    private const val LOCATION: String = "location"
    private const val NETWORK_ZONE: String = "network_zone"
    private const val LABELS: String = "labels"
    private const val DELETE_PROTECTION: String = "delete_protection"
    private const val ID: String = "id"
    private const val IPV4: String = "ipv4"
    private const val IPV6: String = "ipv6"
    private const val SERVICE: String = "service"
    private const val NETWORK_ID: String = "network_id"
    private const val NETWORK_IP: String = "network_ip"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val TARGET: String = "target"

    class Algorithm private constructor(val block: Block) {
        companion object {
            private const val ALGORITHM: String = "algorithm"
            private const val TYPE: String = "type"

            fun reference(index: Int? = null): String = "$ALGORITHM[${Utils.index(index)}]"
            fun type(index: Int? = null): String = "$ALGORITHM[${Utils.index(index)}].$TYPE"
        }

        enum class Type(private val type: String) {
            ROUND_ROBIN("round_robin"),
            LEAST_CONNECTIONS("least_connections");
            override fun toString(): String = type
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder()
            private val typeBuilder: Argument.Builder = Argument.Builder().name(TYPE)

            fun type(type: Type) = apply { blockBuilder.addElement(typeBuilder.value(type.toString()).build()) }
            fun type(ref: TfRef<TfString>) = apply { blockBuilder.addElement(typeBuilder.raw(ref.toString()).build()) }
            fun build() = Algorithm(blockBuilder.type(ALGORITHM).build())
        }

        override fun toString(): String = block.toString()
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val loadBalancerType get() = TfRef<TfString>(reference(LOAD_BALANCER_TYPE))
        val name get() = TfRef<TfString>(reference(NAME))
        val location get() = TfRef<TfString>(reference(LOCATION))
        val ipv4 get() = TfRef<TfString>(reference(IPV4))
        val ipv6 get() = TfRef<TfString>(reference(IPV6))
        val algorithm get() = TfRef<Raw>(reference(Algorithm.reference(0)))
        val algorithmType get() = TfRef<Raw>(reference(Algorithm.type(0)))
        val services get() = TfRef<TfList>(reference(SERVICE)) // TODO? hcloud_load_balancer_service attributes?
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))
        val networkId get() = TfRef<TfNumber>(reference(NETWORK_ID))
        val networkIp get() = TfRef<TfString>(reference(NETWORK_IP))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val loadBalancerTypeBuilder: Argument.Builder = Argument.Builder().name(LOAD_BALANCER_TYPE)
            private val locationBuilder: Argument.Builder = Argument.Builder().name(LOCATION)
            private val networkZoneBuilder: Argument.Builder = Argument.Builder().name(NETWORK_ZONE)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private val deleteProtectionBuilder: Argument.Builder = Argument.Builder().name(DELETE_PROTECTION)
            private var locationOrNetworkZoneSet = false
            private var hasAlgorithm = false
            init { resourceType(HCLOUD_LOAD_BALANCER) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: TfRef<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun loadBalancerType(loadBalancerType: String) = apply { loadBalancerTypeBuilder.value(loadBalancerType) }
            fun loadBalancerType(ref: TfRef<TfString>) = apply { loadBalancerTypeBuilder.raw(ref.toString()) }
            fun location(location: String) = apply { locationOrNetworkZoneSet = true; blockBuilder.addElement(locationBuilder.value(location).build()) }
            fun location(ref: TfRef<TfString>) = apply { locationOrNetworkZoneSet = true; blockBuilder.addElement(locationBuilder.raw(ref.toString()).build()) }
            fun networkZone(networkZone: String) = apply { locationOrNetworkZoneSet = true; blockBuilder.addElement(networkZoneBuilder.value(networkZone).build()) }
            fun networkZone(ref: TfRef<TfString>) = apply { locationOrNetworkZoneSet = true; blockBuilder.addElement(networkZoneBuilder.raw(ref.toString()).build()) }
            fun algorithm(algorithm: Algorithm) = apply { preventDupAlgorithm(); blockBuilder.addElement(algorithm.block) }
            fun labels(labels: TfMap) = apply { blockBuilder.addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { blockBuilder.addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { blockBuilder.addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: TfRef<TfBool>) = apply { blockBuilder.addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                if (!locationOrNetworkZoneSet) {
                    throw IllegalArgumentException("Either location or network_zone must be set!")
                }
                blockBuilder.addElement(nameBuilder.build()).addElement(loadBalancerTypeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }

            private fun preventDupAlgorithm() {
                if (hasAlgorithm) throw IllegalArgumentException("algorithm was already built") else hasAlgorithm = true
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val loadBalancerType get() = TfRef<TfString>(reference(LOAD_BALANCER_TYPE))
        val name get() = TfRef<TfString>(reference(NAME))
        val location get() = TfRef<TfString>(reference(LOCATION))
        val ipv4 get() = TfRef<TfString>(reference(IPV4))
        val ipv6 get() = TfRef<TfString>(reference(IPV6))
        val algorithm get() = TfRef<Raw>(reference(Algorithm.reference(0)))
        val algorithmType get() = TfRef<Raw>(reference(Algorithm.type(0)))
        val targets get() = TfRef<TfList>(reference(TARGET)) // TODO? hcloud_load_balancer_target attributes?
        val services get() = TfRef<TfList>(reference(SERVICE)) // TODO? hcloud_load_balancer_service attributes?
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))
        val networkId get() = TfRef<TfNumber>(reference(NETWORK_ID))
        val networkIp get() = TfRef<TfString>(reference(NETWORK_IP))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_LOAD_BALANCER) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: TfRef<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: TfRef<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { blockBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { blockBuilder.addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                blockBuilder.addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
