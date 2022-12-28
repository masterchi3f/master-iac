package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
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
        val id get() = TfRef<TfNumber>(reference(ID))
        val type get() = TfRef<TfString>(reference(TYPE))
        val name get() = TfRef<TfString>(reference(NAME))
        val serverId get() = TfRef<TfNumber>(reference(SERVER_ID))
        val homeLocation get() = TfRef<TfString>(reference(HOME_LOCATION))
        val description get() = TfRef<TfString>(reference(DESCRIPTION))
        val ipAddress get() = TfRef<TfString>(reference(IP_ADDRESS))
        val ipNetwork get() = TfRef<TfString>(reference(IP_NETWORK))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))

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
            fun type(ref: TfRef<TfString>) = apply { typeBuilder.raw(ref.toString()) }
            fun name(name: String) = apply { blockBuilder.addElement(nameBuilder.value(name).build()) }
            fun name(ref: TfRef<TfString>) = apply { blockBuilder.addElement(nameBuilder.raw(ref.toString()).build()) }
            fun serverId(serverId: Int) = apply { blockBuilder.addElement(serverIdBuilder.value(serverId.toDouble()).build()) }
            fun serverId(ref: TfRef<TfNumber>) = apply { blockBuilder.addElement(serverIdBuilder.raw(ref.toString()).build()) }
            fun homeLocation(homeLocation: String) = apply { blockBuilder.addElement(homeLocationBuilder.value(homeLocation).build()) }
            fun homeLocation(ref: TfRef<TfString>) = apply { blockBuilder.addElement(homeLocationBuilder.raw(ref.toString()).build()) }
            fun description(description: String) = apply { blockBuilder.addElement(descriptionBuilder.value(description).build()) }
            fun description(ref: TfRef<TfString>) = apply { blockBuilder.addElement(descriptionBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { blockBuilder.addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { blockBuilder.addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { blockBuilder.addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: TfRef<TfBool>) = apply { blockBuilder.addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                blockBuilder.addElement(typeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val type get() = TfRef<TfString>(reference(TYPE))
        val name get() = TfRef<TfString>(reference(NAME))
        val serverId get() = TfRef<TfNumber>(reference(SERVER_ID))
        val homeLocation get() = TfRef<TfString>(reference(HOME_LOCATION))
        val description get() = TfRef<TfString>(reference(DESCRIPTION))
        val ipAddress get() = TfRef<TfString>(reference(IP_ADDRESS))
        val ipNetwork get() = TfRef<TfString>(reference(IP_NETWORK))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val idBuilder: Argument.Builder = Argument.Builder().name(ID)
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val ipAddressBuilder: Argument.Builder = Argument.Builder().name(IP_ADDRESS)
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_FLOATING_IP) }

            fun id(id: Int) = apply { blockBuilder.addElement(idBuilder.value(id.toDouble()).build()) }
            fun id(ref: TfRef<TfNumber>) = apply { blockBuilder.addElement(idBuilder.raw(ref.toString()).build()) }
            fun name(name: String) = apply { blockBuilder.addElement(nameBuilder.value(name).build()) }
            fun name(ref: TfRef<TfString>) = apply { blockBuilder.addElement(nameBuilder.raw(ref.toString()).build()) }
            fun ipAddress(ipAddress: String) = apply { blockBuilder.addElement(ipAddressBuilder.value(ipAddress).build()) }
            fun ipAddress(ref: TfRef<TfString>) = apply { blockBuilder.addElement(ipAddressBuilder.raw(ref.toString()).build()) }
            fun withSelector(selector: String) = apply { blockBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { blockBuilder.addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource = DataSource(buildBlock(), buildSelf())
        }
    }
}
