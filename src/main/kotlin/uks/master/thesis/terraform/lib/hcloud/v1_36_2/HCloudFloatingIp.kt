package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudFloatingIp {
    private const val HCLOUD_FLOATING_IP: String = "hcloud_floating_ip"
    private const val TYPE: String = "type"
    private const val NAME: String = "name"
    private const val SERVER_ID: String = "server_id"
    private const val HOME_LOCATION: String = "home_location"
    private const val DESCRIPTION: String = "description"
    private const val LABELS: String = "labels"
    private const val DELETE_PROTECTION: String = "delete_protection"
    private const val ID: String = "id"
    private const val IP_ADDRESS: String = "ip_address"
    private const val IP_NETWORK: String = "ip_network"
    private const val WITH_SELECTOR: String = "with_selector"

    enum class Type(private val type: String) {
        IPV4("ipv4"),
        IPV6("ipv6");
        override fun toString(): String = type
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val type get() = Reference<TfString>(referenceString(TYPE))
        val name get() = Reference<TfString>(referenceString(NAME))
        val serverId get() = Reference<TfNumber>(referenceString(SERVER_ID))
        val homeLocation get() = Reference<TfString>(referenceString(HOME_LOCATION))
        val description get() = Reference<TfString>(referenceString(DESCRIPTION))
        val ipAddress get() = Reference<TfString>(referenceString(IP_ADDRESS))
        val ipNetwork get() = Reference<TfString>(referenceString(IP_NETWORK))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val deleteProtection get() = Reference<TfBool>(referenceString(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val typeBuilder: Argument.Builder = Argument.Builder().name(TYPE)
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val serverIdBuilder: Argument.Builder = Argument.Builder().name(SERVER_ID)
            private val homeLocationBuilder: Argument.Builder = Argument.Builder().name(HOME_LOCATION)
            private val descriptionBuilder: Argument.Builder = Argument.Builder().name(DESCRIPTION)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private val deleteProtectionBuilder: Argument.Builder = Argument.Builder().name(DELETE_PROTECTION)
            init { resourceType(HCLOUD_FLOATING_IP) }

            fun type(type: Type) = apply { typeBuilder.value(type.toString()) }
            fun type(ref: Reference<TfString>) = apply { typeBuilder.raw(ref.toString()) }
            fun name(name: String) = apply { addElement(nameBuilder.value(name).build()) }
            fun name(ref: Reference<TfString>) = apply { addElement(nameBuilder.raw(ref.toString()).build()) }
            fun serverId(serverId: Int) = apply { addElement(serverIdBuilder.value(serverId.toDouble()).build()) }
            fun serverId(ref: Reference<TfNumber>) = apply { addElement(serverIdBuilder.raw(ref.toString()).build()) }
            fun homeLocation(homeLocation: String) = apply { addElement(homeLocationBuilder.value(homeLocation).build()) }
            fun homeLocation(ref: Reference<TfString>) = apply { addElement(homeLocationBuilder.raw(ref.toString()).build()) }
            fun description(description: String) = apply { addElement(descriptionBuilder.value(description).build()) }
            fun description(ref: Reference<TfString>) = apply { addElement(descriptionBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: Reference<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: Reference<TfBool>) = apply { addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(typeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val type get() = Reference<TfString>(referenceString(TYPE))
        val name get() = Reference<TfString>(referenceString(NAME))
        val serverId get() = Reference<TfNumber>(referenceString(SERVER_ID))
        val homeLocation get() = Reference<TfString>(referenceString(HOME_LOCATION))
        val description get() = Reference<TfString>(referenceString(DESCRIPTION))
        val ipAddress get() = Reference<TfString>(referenceString(IP_ADDRESS))
        val ipNetwork get() = Reference<TfString>(referenceString(IP_NETWORK))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val deleteProtection get() = Reference<TfBool>(referenceString(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val idBuilder: Argument.Builder = Argument.Builder().name(ID)
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val ipAddressBuilder: Argument.Builder = Argument.Builder().name(IP_ADDRESS)
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_FLOATING_IP) }

            fun id(id: Int) = apply { addElement(idBuilder.value(id.toDouble()).build()) }
            fun id(ref: Reference<TfNumber>) = apply { addElement(idBuilder.raw(ref.toString()).build()) }
            fun name(name: String) = apply { addElement(nameBuilder.value(name).build()) }
            fun name(ref: Reference<TfString>) = apply { addElement(nameBuilder.raw(ref.toString()).build()) }
            fun ipAddress(ipAddress: String) = apply { addElement(ipAddressBuilder.value(ipAddress).build()) }
            fun ipAddress(ref: Reference<TfString>) = apply { addElement(ipAddressBuilder.raw(ref.toString()).build()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource = DataSource(buildBlock(), buildSelf())
        }
    }
}
