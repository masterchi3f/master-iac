package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList

class Cloud private constructor(
    val block: Block
): Element {
    private companion object {
        const val CLOUD: String = "cloud"
        const val ORGANIZATION: String = "organization"
        const val HOSTNAME: String = "hostname"
        const val WORKSPACES: String = "workspaces"
        const val NAME: String = "name"
        const val TAGS: String = "tags"
    }

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val hostnameBuilder: Argument.Builder = Argument.Builder().name(HOSTNAME)
        private val workspacesStrategyBuilder: Argument.Builder = Argument.Builder()
        private lateinit var _organization: String

        fun organization(organization: String) = apply { preventDupOrganization(); _organization = organization }
        fun hostname(hostname: String) = apply { blockBuilder.addElement(hostnameBuilder.value(hostname).build()) }
        fun workspacesName(name: String) = apply { workspacesStrategyBuilder.name(NAME).value(name) }
        fun workspacesTags(tags: TfList) = apply { workspacesStrategyBuilder.name(TAGS).value(tags) }
        fun build() = Cloud(
            blockBuilder
                .type(CLOUD)
                .addElement(Argument.Builder().name(ORGANIZATION).value(_organization).build())
                .addElement(Block.Builder().type(WORKSPACES).addElement(workspacesStrategyBuilder.build()).build())
                .build()
        )

        private fun preventDupOrganization() {
            require(!::_organization.isInitialized) {"organization was already set to $_organization!"}
        }
    }

    override fun toString(): String = block.toString()
}
