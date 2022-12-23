package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.elements.MultiLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineSymbol

open class Resource protected constructor(
    private val block: Block,
    private val self: String
): Element, Child {
    private companion object {
        const val RESOURCE: String = "resource"
        const val PROVIDER: String = "provider"
    }

    @Suppress("UNCHECKED_CAST")
    open class GBuilder<T> {
        private val providerBuilder: Argument.Builder = Argument.Builder().name(PROVIDER)
        private lateinit var _type: Identifier
        private lateinit var _name: Identifier
        protected val blockBuilder: Block.Builder = Block.Builder()

        fun resourceType(type: String): T = apply { preventDupType(); _type = Identifier(type) } as T
        fun resourceName(name: String): T = apply { preventDupName(); _name = Identifier(name) } as T
        fun addElement(block: Block): T = apply { blockBuilder.addElement(block) } as T
        fun addElement(argument: Argument): T = apply { blockBuilder.addElement(argument) } as T
        fun addElement(symbol: OneLineSymbol, text: String): T = apply { blockBuilder.addElement(symbol, text) } as T
        fun addElement(multiLineComment: MultiLineComment): T = apply { blockBuilder.addElement(multiLineComment) } as T
        fun provider(provider: Provider, alternate: Boolean = false): T =
            apply { blockBuilder.addElement(providerBuilder.value(provider, alternate).build()) } as T
        open fun build() = Resource(buildBlock(), buildSelf())
        protected fun buildBlock(): Block = blockBuilder.type(RESOURCE).addLabel(_type.toString()).addLabel(_name.toString()).build()
        protected fun buildSelf(): String = "$_type.$_name"

        private fun preventDupType() {
            if (::_type.isInitialized) throw IllegalArgumentException("type was already set to $_type!")
        }

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    class Builder: GBuilder<Builder>()

    fun reference(attribute: String? = null): String = attribute?.let { "$self.$it" } ?: self

    override fun toString(): String = block.toString()
}
