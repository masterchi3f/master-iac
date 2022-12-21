package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.SubModule
import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap

class OutputVariable private constructor(
    private val block: Block,
    private val self: String
): Element, Child {
    private companion object {
        const val OUTPUT: String = "output"
        const val VALUE: String = "value"
        const val SENSITIVE: String = "sensitive"
    }

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val valueBuilder: Argument.Builder = Argument.Builder().name(VALUE)
        private val sensitiveBuilder: Argument.Builder = Argument.Builder().name(SENSITIVE)
        private lateinit var _name: Identifier

        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun nullValue() = apply { blockBuilder.addElement(valueBuilder.nullValue().build()) }
        fun value(bool: Boolean) = apply { blockBuilder.addElement(valueBuilder.value(bool).build()) }
        fun value(number: Double) = apply { blockBuilder.addElement(valueBuilder.value(number).build()) }
        fun value(string: String) = apply { blockBuilder.addElement(valueBuilder.value(string).build()) }
        fun value(list: TfList) = apply { blockBuilder.addElement(valueBuilder.value(list).build()) }
        fun value(map: TfMap) = apply { blockBuilder.addElement(valueBuilder.value(map).build()) }
        fun value(resource: Resource, attribute: String? = null) = apply { blockBuilder.addElement(valueBuilder.value(resource, attribute).build()) }
        fun value(dataSource: DataSource, attribute: String? = null) = apply { blockBuilder.addElement(valueBuilder.value(dataSource, attribute).build()) }
        fun value(subModule: SubModule, outputVariable: OutputVariable) = apply { blockBuilder.addElement(valueBuilder.value(subModule, outputVariable).build()) }
        fun sensitive() = apply { blockBuilder.addElement(sensitiveBuilder.value(true).build()) }
        fun build() = OutputVariable(
            blockBuilder.type(OUTPUT).addLabel(_name.toString()).build(),
            _name.toString()
        )

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    fun name(): String {
        return self
    }

    override fun toString(): String {
        return block.toString()
    }
}