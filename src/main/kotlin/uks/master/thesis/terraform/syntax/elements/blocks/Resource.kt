package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.elements.MultiLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineSymbol

class Resource private constructor(
    private val block: Block,
    private val self: String
): Element, Child {
    private companion object {
        const val RESOURCE: String = "resource"
        const val PROVIDER: String = "provider"
    }

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val providerBuilder: Argument.Builder = Argument.Builder().name(PROVIDER)
        private lateinit var _type: Identifier
        private lateinit var _name: Identifier

        fun type(type: String) = apply { preventDupType(); _type = Identifier(type) }
        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun addElement(block: Block) = apply { blockBuilder.addElement(block) }
        fun addElement(argument: Argument) = apply { blockBuilder.addElement(argument) }
        fun addElement(symbol: OneLineSymbol, text: String) = apply { blockBuilder.addElement(symbol, text) }
        fun addElement(multiLineComment: MultiLineComment) = apply { blockBuilder.addElement(multiLineComment) }
        fun provider(provider: Provider, alternate: Boolean = false) =
            apply { blockBuilder.addElement(providerBuilder.value(provider, alternate).build()) }
        fun build() = Resource(
            blockBuilder.type(RESOURCE).addLabel(_type.toString()).addLabel(_name.toString()).build(),
            "$_type.$_name"
        )

        private fun preventDupType() {
            if (::_type.isInitialized) throw IllegalArgumentException("type was already set to $_type!")
        }

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    fun reference(attribute: String? = null): String = attribute?.let { "$self.$it" } ?: self

    override fun toString(): String = block.toString()
}
