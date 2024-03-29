package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference

object HCloudFloatingIpAssignment {
    private const val HCLOUD_FLOATING_IP_ASSIGNMENT: String = "hcloud_floating_ip_assignment"
    private const val FLOATING_IP_ID: String = "floating_ip_id"
    private const val SERVER_ID: String = "server_id"
    private const val ID: String = "id"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val floatingIpId get() = Reference<TfNumber>(referenceString(FLOATING_IP_ID))
        val serverId get() = Reference<TfNumber>(referenceString(SERVER_ID))

        class Builder: GBuilder<Builder>() {
            private val floatingIpIdBuilder: Argument.Builder = Argument.Builder().name(FLOATING_IP_ID)
            private val serverIdBuilder: Argument.Builder = Argument.Builder().name(SERVER_ID)
            init { resourceType(HCLOUD_FLOATING_IP_ASSIGNMENT) }

            fun floatingIpId(floatingIpId: Int) = apply { floatingIpIdBuilder.value(floatingIpId.toDouble()) }
            fun floatingIpId(ref: Reference<TfNumber>) = apply { floatingIpIdBuilder.raw(ref.toString()) }
            fun serverId(serverId: Int) = apply { serverIdBuilder.value(serverId.toDouble()) }
            fun serverId(ref: Reference<TfNumber>) = apply { serverIdBuilder.raw(ref.toString()) }
            override fun build(): Resource {
                addElement(floatingIpIdBuilder.build())
                addElement(serverIdBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
