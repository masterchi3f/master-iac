package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap

class InputVariable private constructor(
    private val block: Block,
    private val self: String
): Element, Child {
    private companion object {
        const val VARIABLE: String = "variable"
        const val VAR: String = "var"
        const val DEFAULT: String = "default"
        const val SENSITIVE: String = "sensitive"
    }

    val reference get() = "$VAR.$self"
    val name get() = self

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val defaultBuilder: Argument.Builder = Argument.Builder().name(DEFAULT)
        private val sensitiveBuilder: Argument.Builder = Argument.Builder().name(SENSITIVE)
        private lateinit var _name: Identifier

        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun defaultAsNull() = apply { blockBuilder.addElement(defaultBuilder.nullValue().build()) }
        fun default(bool: Boolean) = apply { blockBuilder.addElement(defaultBuilder.value(bool).build()) }
        fun default(number: Double) = apply { blockBuilder.addElement(defaultBuilder.value(number).build()) }
        fun default(string: String) = apply { blockBuilder.addElement(defaultBuilder.value(string).build()) }
        fun default(list: TfList) = apply { blockBuilder.addElement(defaultBuilder.value(list).build()) }
        fun default(map: TfMap) = apply { blockBuilder.addElement(defaultBuilder.value(map).build()) }
        fun sensitive() = apply { blockBuilder.addElement(sensitiveBuilder.value(true).build()) }
        fun build() = InputVariable(
            blockBuilder.type(VARIABLE).addLabel(_name.toString()).build(),
            _name.toString()
        )

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    override fun toString(): String = block.toString()
}
