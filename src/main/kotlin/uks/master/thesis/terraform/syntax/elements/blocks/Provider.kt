package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block

class Provider private constructor(
    private val block: Block,
    private val self: String,
    val alias: String?
): Element {
    private companion object {
        const val PROVIDER: String = "provider"
        const val ALIAS: String = "alias"
    }

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val aliasBuilder: Argument.Builder = Argument.Builder().name(ALIAS)
        private lateinit var _name: Identifier
        private var _alias: String? = null

        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun alias(alias: String) = apply { preventDupAlias(); _alias = Identifier(alias).toString() }
        fun addConfig(argument: Argument) = apply { blockBuilder.addElement(argument) }
        fun build(): Provider = run {
            _alias?.let {
                blockBuilder.addElement(aliasBuilder.value(it).build())
            }
            Provider(
                blockBuilder.type(PROVIDER).addLabel(_name.toString()).build(),
                _name.toString(),
                _alias
            )
        }

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }

        private fun preventDupAlias() {
            _alias?.let { throw IllegalArgumentException("alias was already set to $it!") }
        }
    }

    fun name(): String {
        return self
    }

    override fun toString(): String {
        return block.toString()
    }
}
