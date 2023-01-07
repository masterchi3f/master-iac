package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudVolume {
    private const val HCLOUD_VOLUME: String = "hcloud_volume"
    private const val NAME: String = "name"
    private const val SIZE: String = "size"
    private const val SERVER_ID: String = "server_id"
    private const val LOCATION: String = "location"
    private const val AUTOMOUNT: String = "automount"
    private const val FORMAT: String = "format"
    private const val DELETE_PROTECTION: String = "delete_protection"
    private const val ID: String = "id"
    private const val LABELS: String = "labels"
    private const val LINUX_DEVICE: String = "linux_device"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val WITH_STATUS: String = "with_status"

    enum class Format(private val format: String) {
        XFS("xfs"),
        EXT4("ext4");
        override fun toString(): String = format
    }

    enum class Status(private val status: String) {
        CREATING("creating"),
        AVAILABLE("available");
        override fun toString(): String = status
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(referenceString(ID))
        val name get() = TfRef<TfString>(referenceString(NAME))
        val size get() = TfRef<TfNumber>(referenceString(SIZE))
        val location get() = TfRef<TfString>(referenceString(LOCATION))
        val serverId get() = TfRef<TfNumber>(referenceString(SERVER_ID))
        val labels get() = TfRef<TfMap>(referenceString(LABELS))
        val linuxDevice get() = TfRef<TfString>(referenceString(LINUX_DEVICE))
        val deleteProtection get() = TfRef<TfBool>(referenceString(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val sizeBuilder: Argument.Builder = Argument.Builder().name(SIZE)
            private val serverIdBuilder: Argument.Builder = Argument.Builder().name(SERVER_ID)
            private val locationBuilder: Argument.Builder = Argument.Builder().name(LOCATION)
            private val automountBuilder: Argument.Builder = Argument.Builder().name(AUTOMOUNT)
            private val formatBuilder: Argument.Builder = Argument.Builder().name(FORMAT)
            private val deleteProtectionBuilder: Argument.Builder = Argument.Builder().name(DELETE_PROTECTION)
            init { resourceType(HCLOUD_VOLUME) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: TfRef<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun size(size: Int) = apply { sizeBuilder.value(size.toDouble()) }
            fun size(ref: TfRef<TfNumber>) = apply { sizeBuilder.raw(ref.toString()) }
            fun serverId(serverId: Int) = apply { addElement(serverIdBuilder.value(serverId.toDouble()).build()) }
            fun serverId(ref: TfRef<TfNumber>) = apply { addElement(serverIdBuilder.raw(ref.toString()).build()) }
            fun location(location: String) = apply { addElement(locationBuilder.value(location).build()) }
            fun location(ref: TfRef<TfString>) = apply { addElement(locationBuilder.raw(ref.toString()).build()) }
            fun automount(automount: Boolean) = apply { addElement(automountBuilder.value(automount).build()) }
            fun automount(ref: TfRef<TfBool>) = apply { addElement(automountBuilder.raw(ref.toString()).build()) }
            fun format(format: Format) = apply { addElement(formatBuilder.value(format.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: TfRef<TfBool>) = apply { addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(nameBuilder.build())
                addElement(sizeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(referenceString(ID))
        val name get() = TfRef<TfString>(referenceString(NAME))
        val size get() = TfRef<TfNumber>(referenceString(SIZE))
        val location get() = TfRef<TfString>(referenceString(LOCATION))
        val serverId get() = TfRef<TfNumber>(referenceString(SERVER_ID))
        val labels get() = TfRef<TfMap>(referenceString(LABELS))
        val linuxDevice get() = TfRef<TfString>(referenceString(LINUX_DEVICE))
        val deleteProtection get() = TfRef<TfBool>(referenceString(DELETE_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val withStatusBuilder: Argument.Builder = Argument.Builder().name(WITH_STATUS)
            init { dataSource(HCLOUD_VOLUME) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: TfRef<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: TfRef<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun withStatus(withStatus: List<Status>) = apply {
                val set: Set<Status> = withStatus.toSet()
                val tfListBuilder: TfList.Builder = TfList.Builder()
                set.forEach { tfListBuilder.add(it.toString()) }
                addElement(withStatusBuilder.value(tfListBuilder.build()).build())
            }
            override fun build(): DataSource {
                addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
