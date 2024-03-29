package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudSnapshot {
    private const val HCLOUD_SNAPSHOT: String = "hcloud_snapshot"
    private const val SERVER_ID: String = "server_id"
    private const val DESCRIPTION: String = "description"
    private const val LABELS: String = "labels"
    private const val ID: String = "id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val serverId get() = Reference<TfNumber>(referenceString(SERVER_ID))
        val description get() = Reference<TfString>(referenceString(DESCRIPTION))
        val labels get() = Reference<TfMap>(referenceString(LABELS))

        class Builder: GBuilder<Builder>() {
            private val serverIdBuilder: Argument.Builder = Argument.Builder().name(SERVER_ID)
            private val descriptionBuilder: Argument.Builder = Argument.Builder().name(DESCRIPTION)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            init { resourceType(HCLOUD_SNAPSHOT) }

            fun serverId(serverId: Int) = apply { serverIdBuilder.value(serverId.toDouble()) }
            fun serverId(ref: Reference<TfNumber>) = apply { serverIdBuilder.raw(ref.toString()) }
            fun description(description: String) = apply { addElement(descriptionBuilder.value(description).build()) }
            fun description(ref: Reference<TfString>) = apply { addElement(descriptionBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: Reference<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(serverIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
