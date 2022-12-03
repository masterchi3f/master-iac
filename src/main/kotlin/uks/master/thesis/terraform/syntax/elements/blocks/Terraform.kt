package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block

class Terraform private constructor(
    private val block: Block
): Element {
    private companion object {
        const val TERRAFORM: String = "terraform"
        const val REQUIRED_VERSION: String = "required_version"
    }

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val requiredVersionBuilder: Argument.Builder = Argument.Builder().name(REQUIRED_VERSION)
        private var _requiredProviders: RequiredProviders? = null
        private var _cloud: Cloud? = null

        fun requiredVersion(requiredVersion: String) = apply { blockBuilder.addElement(requiredVersionBuilder.value(requiredVersion).build()) }
        fun requiredProviders(requiredProviders: RequiredProviders) = apply { preventDupReqProv(); _requiredProviders = requiredProviders }
        fun cloud(cloud: Cloud) = apply { preventDupCloud(); _cloud = cloud }
        fun build(): Terraform = run {
            _requiredProviders?.let {
                blockBuilder.addElement(it.block)
            }
            _cloud?.let {
                blockBuilder.addElement(it.block)
            }
            Terraform(blockBuilder.type(TERRAFORM).build())
        }

        private fun preventDupReqProv() {
            _requiredProviders?.let { throw IllegalArgumentException("requiredProviders was already set to ${System.lineSeparator()}$it") }
        }

        private fun preventDupCloud() {
            _cloud?.let { throw IllegalArgumentException("cloud was already set to ${System.lineSeparator()}$it") }
        }
    }

    override fun toString(): String {
        return block.toString()
    }
}
