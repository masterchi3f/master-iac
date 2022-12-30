package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudPrimaryIp {
    private const val HCLOUD_PRIMARY_IP: String = "hcloud_primary_ip"
    private const val ID: String = "id"
    private const val TYPE: String = "type"
    private const val NAME: String = "name"
    private const val DATACENTER: String = "datacenter"
    private const val AUTO_DELETE: String = "auto_delete"
    private const val LABELS: String = "labels"
    private const val ASSIGNEE_ID: String = "assignee_id"
    private const val ASSIGNEE_TYPE: String = "assignee_type"
    private const val DELETE_PROTECTION: String = "delete_protection"
    private const val IP_ADDRESS: String = "ip_address"
    private const val WITH_SELECTOR: String = "with_selector"

    enum class Type(private val type: String) {
        IPV4("ipv4"),
        IPV6("ipv6");
        override fun toString(): String = type
    }

    enum class AssigneeType(private val type: String) {
        SERVER("server");
        override fun toString(): String = type
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val type get() = TfRef<TfString>(reference(TYPE))
        val datacenter get() = TfRef<TfString>(reference(DATACENTER))
        val name get() = TfRef<TfString>(reference(NAME))
        val autoDelete get() = TfRef<TfBool>(reference(AUTO_DELETE))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val ipAddress get() = TfRef<TfString>(reference(IP_ADDRESS))
        val assigneeId get() = TfRef<TfNumber>(reference(ASSIGNEE_ID))
        val assigneeType get() = TfRef<TfString>(reference(ASSIGNEE_TYPE))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val typeBuilder: Argument.Builder = Argument.Builder().name(TYPE)
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val autoDeleteBuilder: Argument.Builder = Argument.Builder().name(AUTO_DELETE)
            private val assigneeTypeBuilder: Argument.Builder = Argument.Builder().name(ASSIGNEE_TYPE)
            private val datacenterBuilder: Argument.Builder = Argument.Builder().name(DATACENTER)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private val deleteProtectionBuilder: Argument.Builder = Argument.Builder().name(DELETE_PROTECTION)
            init { resourceType(HCLOUD_PRIMARY_IP) }

            fun type(type: Type) = apply { typeBuilder.value(type.toString()) }
            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: TfRef<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun autoDelete(autoDelete: Boolean) = apply { autoDeleteBuilder.value(autoDelete) }
            fun autoDelete(ref: TfRef<TfBool>) = apply { autoDeleteBuilder.raw(ref.toString()) }
            fun assigneeType(assigneeType: AssigneeType) = apply { assigneeTypeBuilder.value(assigneeType.toString()) }
            fun datacenter(datacenter: String) = apply { blockBuilder.addElement(datacenterBuilder.value(datacenter).build()) }
            fun datacenter(ref: TfRef<TfString>) = apply { blockBuilder.addElement(datacenterBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { blockBuilder.addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { blockBuilder.addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { blockBuilder.addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: TfRef<TfBool>) = apply { blockBuilder.addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                blockBuilder
                    .addElement(typeBuilder.build())
                    .addElement(nameBuilder.build())
                    .addElement(autoDeleteBuilder.build())
                    .addElement(assigneeTypeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val type get() = TfRef<TfString>(reference(TYPE))
        val datacenter get() = TfRef<TfString>(reference(DATACENTER))
        val name get() = TfRef<TfString>(reference(NAME))
        val autoDelete get() = TfRef<TfBool>(reference(AUTO_DELETE))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val ipAddress get() = TfRef<TfString>(reference(IP_ADDRESS))
        val assigneeId get() = TfRef<TfNumber>(reference(ASSIGNEE_ID))
        val assigneeType get() = TfRef<TfString>(reference(ASSIGNEE_TYPE))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val idOrNameOrIpAddressBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_PRIMARY_IP) }

            fun id(id: Int) = apply { idOrNameOrIpAddressBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: TfRef<TfNumber>) = apply { idOrNameOrIpAddressBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameOrIpAddressBuilder.name(NAME).value(name) }
            fun name(ref: TfRef<TfString>) = apply { idOrNameOrIpAddressBuilder.name(NAME).raw(ref.toString()) }
            fun ipAddress(ipAddress: String) = apply { idOrNameOrIpAddressBuilder.name(IP_ADDRESS).value(ipAddress) }
            fun ipAddress(ref: TfRef<TfString>) = apply { idOrNameOrIpAddressBuilder.name(IP_ADDRESS).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { blockBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { blockBuilder.addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                blockBuilder.addElement(idOrNameOrIpAddressBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
