package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference

object HCloudVolumeAttachment {
    private const val HCLOUD_VOLUME_ATTACHMENT: String = "hcloud_volume_attachment"
    private const val VOLUME_ID: String = "volume_id"
    private const val SERVER_ID: String = "server_id"
    private const val AUTOMOUNT: String = "automount"
    private const val ID: String = "id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val volumeId get() = Reference<TfNumber>(referenceString(VOLUME_ID))
        val serverId get() = Reference<TfNumber>(referenceString(SERVER_ID))

        class Builder: GBuilder<Builder>() {
            private val volumeIdBuilder: Argument.Builder = Argument.Builder().name(VOLUME_ID)
            private val serverIdBuilder: Argument.Builder = Argument.Builder().name(SERVER_ID)
            private val automountBuilder: Argument.Builder = Argument.Builder().name(AUTOMOUNT)
            init { resourceType(HCLOUD_VOLUME_ATTACHMENT) }

            fun volumeId(volumeId: Int) = apply { volumeIdBuilder.value(volumeId.toDouble()) }
            fun volumeId(ref: Reference<TfNumber>) = apply { volumeIdBuilder.raw(ref.toString()) }
            fun serverId(serverId: Int) = apply { serverIdBuilder.value(serverId.toDouble()) }
            fun serverId(ref: Reference<TfNumber>) = apply { serverIdBuilder.raw(ref.toString()) }
            fun automount(automount: Boolean) = apply { addElement(automountBuilder.value(automount).build()) }
            fun automount(ref: Reference<TfBool>) = apply { addElement(automountBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(volumeIdBuilder.build())
                addElement(serverIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
