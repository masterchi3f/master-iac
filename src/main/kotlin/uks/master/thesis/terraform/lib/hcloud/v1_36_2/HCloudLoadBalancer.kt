package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
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

            fun reference(index: Int? = null): String = Utils.splatExp(ALGORITHM, index)
            fun type(index: Int? = null): String = Utils.splatExp(ALGORITHM, index, TYPE)
        }

        enum class Type(private val type: String) {
            ROUND_ROBIN("round_robin"),
            LEAST_CONNECTIONS("least_connections");
            override fun toString(): String = type
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder().type(ALGORITHM)
            private val typeBuilder: Argument.Builder = Argument.Builder().name(TYPE)

            fun type(type: Type) = apply { blockBuilder.addElement(typeBuilder.value(type.toString()).build()) }
            fun type(ref: Reference<TfString>) = apply { blockBuilder.addElement(typeBuilder.raw(ref.toString()).build()) }
            fun build() = Algorithm(blockBuilder.build())
        }

        override fun toString(): String = block.toString()
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val loadBalancerType get() = Reference<TfString>(referenceString(LOAD_BALANCER_TYPE))
        val name get() = Reference<TfString>(referenceString(NAME))
        val location get() = Reference<TfString>(referenceString(LOCATION))
        val ipv4 get() = Reference<TfString>(referenceString(IPV4))
        val ipv6 get() = Reference<TfString>(referenceString(IPV6))
        val algorithm get() = Reference<Raw>(referenceString(Algorithm.reference(0)))
        val algorithmType get() = Reference<Raw>(referenceString(Algorithm.type(0)))
        val services get() = Reference<TfList>(referenceString(SERVICE))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val deleteProtection get() = Reference<TfBool>(referenceString(DELETE_PROTECTION))
        val networkId get() = Reference<TfNumber>(referenceString(NETWORK_ID))
        val networkIp get() = Reference<TfString>(referenceString(NETWORK_IP))

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
            fun name(ref: Reference<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun loadBalancerType(loadBalancerType: String) = apply { loadBalancerTypeBuilder.value(loadBalancerType) }
            fun loadBalancerType(ref: Reference<TfString>) = apply { loadBalancerTypeBuilder.raw(ref.toString()) }
            fun location(location: String) = apply { locationOrNetworkZoneSet = true; addElement(locationBuilder.value(location).build()) }
            fun location(ref: Reference<TfString>) = apply { locationOrNetworkZoneSet = true; addElement(locationBuilder.raw(ref.toString()).build()) }
            fun networkZone(networkZone: String) = apply { locationOrNetworkZoneSet = true; addElement(networkZoneBuilder.value(networkZone).build()) }
            fun networkZone(ref: Reference<TfString>) = apply { locationOrNetworkZoneSet = true; addElement(networkZoneBuilder.raw(ref.toString()).build()) }
            fun algorithm(algorithm: Algorithm) = apply { preventDupAlgorithm(); addElement(algorithm.block) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: Reference<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: Reference<TfBool>) = apply { addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                require(locationOrNetworkZoneSet) {"Either location or networkZone must be set!"}
                addElement(nameBuilder.build())
                addElement(loadBalancerTypeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }

            private fun preventDupAlgorithm() {
                require(!hasAlgorithm) {"algorithm was already built"}
                hasAlgorithm = true
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val loadBalancerType get() = Reference<TfString>(referenceString(LOAD_BALANCER_TYPE))
        val name get() = Reference<TfString>(referenceString(NAME))
        val location get() = Reference<TfString>(referenceString(LOCATION))
        val ipv4 get() = Reference<TfString>(referenceString(IPV4))
        val ipv6 get() = Reference<TfString>(referenceString(IPV6))
        val algorithm get() = Reference<Raw>(referenceString(Algorithm.reference(0)))
        val algorithmType get() = Reference<Raw>(referenceString(Algorithm.type(0)))
        val targets get() = Reference<TfList>(referenceString(TARGET))
        val services get() = Reference<TfList>(referenceString(SERVICE))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val deleteProtection get() = Reference<TfBool>(referenceString(DELETE_PROTECTION))
        val networkId get() = Reference<TfNumber>(referenceString(NETWORK_ID))
        val networkIp get() = Reference<TfString>(referenceString(NETWORK_IP))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_LOAD_BALANCER) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: Reference<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: Reference<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
