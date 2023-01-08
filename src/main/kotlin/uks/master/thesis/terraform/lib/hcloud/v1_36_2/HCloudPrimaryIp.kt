package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
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
        val id get() = Reference<TfNumber>(referenceString(ID))
        val type get() = Reference<TfString>(referenceString(TYPE))
        val datacenter get() = Reference<TfString>(referenceString(DATACENTER))
        val name get() = Reference<TfString>(referenceString(NAME))
        val autoDelete get() = Reference<TfBool>(referenceString(AUTO_DELETE))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val ipAddress get() = Reference<TfString>(referenceString(IP_ADDRESS))
        val assigneeId get() = Reference<TfNumber>(referenceString(ASSIGNEE_ID))
        val assigneeType get() = Reference<TfString>(referenceString(ASSIGNEE_TYPE))
        val deleteProtection get() = Reference<TfBool>(referenceString(DELETE_PROTECTION))

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
            fun name(ref: Reference<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun autoDelete(autoDelete: Boolean) = apply { autoDeleteBuilder.value(autoDelete) }
            fun autoDelete(ref: Reference<TfBool>) = apply { autoDeleteBuilder.raw(ref.toString()) }
            fun assigneeType(assigneeType: AssigneeType) = apply { assigneeTypeBuilder.value(assigneeType.toString()) }
            fun datacenter(datacenter: String) = apply { addElement(datacenterBuilder.value(datacenter).build()) }
            fun datacenter(ref: Reference<TfString>) = apply { addElement(datacenterBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: Reference<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: Reference<TfBool>) = apply { addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(typeBuilder.build())
                addElement(nameBuilder.build())
                addElement(autoDeleteBuilder.build())
                addElement(assigneeTypeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val type get() = Reference<TfString>(referenceString(TYPE))
        val datacenter get() = Reference<TfString>(referenceString(DATACENTER))
        val name get() = Reference<TfString>(referenceString(NAME))
        val autoDelete get() = Reference<TfBool>(referenceString(AUTO_DELETE))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val ipAddress get() = Reference<TfString>(referenceString(IP_ADDRESS))
        val assigneeId get() = Reference<TfNumber>(referenceString(ASSIGNEE_ID))
        val assigneeType get() = Reference<TfString>(referenceString(ASSIGNEE_TYPE))
        val deleteProtection get() = Reference<TfBool>(referenceString(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val idOrNameOrIpAddressBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_PRIMARY_IP) }

            fun id(id: Int) = apply { idOrNameOrIpAddressBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: Reference<TfNumber>) = apply { idOrNameOrIpAddressBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameOrIpAddressBuilder.name(NAME).value(name) }
            fun name(ref: Reference<TfString>) = apply { idOrNameOrIpAddressBuilder.name(NAME).raw(ref.toString()) }
            fun ipAddress(ipAddress: String) = apply { idOrNameOrIpAddressBuilder.name(IP_ADDRESS).value(ipAddress) }
            fun ipAddress(ref: Reference<TfString>) = apply { idOrNameOrIpAddressBuilder.name(IP_ADDRESS).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                addElement(idOrNameOrIpAddressBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
