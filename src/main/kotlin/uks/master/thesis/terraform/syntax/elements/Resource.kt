package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.syntax.Identifier

class Resource private constructor(
    private val block: Block
) {
    companion object {
        const val RESOURCE: String = "resource"
    }

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private lateinit var _type: Identifier
        private lateinit var _name: Identifier

        fun type(type: String) = apply { preventDupType(); _type = Identifier(type) }
        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun addElement(block: Block) = apply { blockBuilder.addElement(block) }
        fun addElement(argument: Argument) = apply { blockBuilder.addElement(argument) }
        fun addElement(symbol: OneLineSymbol, text: String) = apply { blockBuilder.addElement(symbol, text) }
        fun addElement(multiLineComment: MultiLineComment) = apply { blockBuilder.addElement(multiLineComment) }
        fun build() = Resource(blockBuilder.type(RESOURCE).addLabel(_type.toString()).addLabel(_name.toString()).build())

        private fun preventDupType() {
            if (::_type.isInitialized) throw IllegalArgumentException("type was already set to $_type!")
        }

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    override fun toString(): String {
        return block.toString()
    }
}
