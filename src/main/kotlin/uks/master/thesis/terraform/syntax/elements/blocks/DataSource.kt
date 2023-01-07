package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.elements.MultiLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineSymbol
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfRef

open class DataSource protected constructor(
    private val block: Block,
    private val self: String
): Element, Child {
    private companion object {
        const val DATA: String = "data"
        const val PROVIDER: String = "provider"
    }

    @Suppress("UNCHECKED_CAST")
    open class GBuilder<T> {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val providerBuilder: Argument.Builder = Argument.Builder().name(PROVIDER)
        private lateinit var _source: Identifier
        private lateinit var _name: Identifier

        fun dataSource(source: String): T = apply { preventDupSource(); _source = Identifier(source) } as T
        fun dataName(name: String): T = apply { preventDupName(); _name = Identifier(name) } as T
        fun addElement(block: Block): T = apply { blockBuilder.addElement(block) } as T
        fun addElement(argument: Argument): T = apply { blockBuilder.addElement(argument) } as T
        fun addElement(symbol: OneLineSymbol, text: String): T = apply { blockBuilder.addElement(symbol, text) } as T
        fun addElement(multiLineComment: MultiLineComment): T = apply { blockBuilder.addElement(multiLineComment) } as T
        fun provider(provider: Provider, alternate: Boolean = false): T =
            apply { blockBuilder.addElement(providerBuilder.value(provider, alternate).build()) } as T
        open fun build() = DataSource(buildBlock(), buildSelf())
        protected fun buildBlock(): Block = blockBuilder.type(DATA).addLabel(_source.toString()).addLabel(_name.toString()).build()
        protected fun buildSelf(): String = "$DATA.$_source.$_name"

        private fun preventDupSource() {
            if (::_source.isInitialized) throw IllegalArgumentException("source was already set to $_source!")
        }

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    class Builder: GBuilder<Builder>()

    fun referenceString(attribute: String? = null): String = attribute?.let { "$self.$it" } ?: self
    fun reference(): TfRef<Raw> = TfRef(self)
    fun <S: Expression>reference(attribute: String): TfRef<S> = TfRef("$self.$attribute")

    override fun toString(): String = block.toString()
}
