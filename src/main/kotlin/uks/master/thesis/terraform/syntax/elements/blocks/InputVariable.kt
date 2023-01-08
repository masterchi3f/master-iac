package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

class InputVariable<T : Expression> private constructor(
    private val block: Block,
    private val self: String
): Element, Child {
    private companion object {
        const val VARIABLE: String = "variable"
        const val VAR: String = "var"
        const val DEFAULT: String = "default"
        const val SENSITIVE: String = "sensitive"
    }

    val reference get() = TfRef<T>("$VAR.$self")
    val name get() = self

    class Builder<T: Expression>(private val expClass: Class<T>) {
        companion object {
            inline operator fun <reified T : Expression>invoke() = Builder(T::class.java)
        }
        private val blockBuilder: Block.Builder = Block.Builder()
        private val defaultBuilder: Argument.Builder = Argument.Builder().name(DEFAULT)
        private val sensitiveBuilder: Argument.Builder = Argument.Builder().name(SENSITIVE)
        private lateinit var _name: Identifier
        init { checkAtInit(listOf(TfBool(true), TfNumber(0.0), TfString(""), TfList.Builder().build(), TfMap.Builder().build())) }

        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun defaultAsNull() = apply { blockBuilder.addElement(defaultBuilder.nullValue().build()) }
        fun default(bool: Boolean) = apply { checkType(TfBool(bool)); blockBuilder.addElement(defaultBuilder.value(bool).build()) }
        fun default(number: Double) = apply { checkType(TfNumber(number)); blockBuilder.addElement(defaultBuilder.value(number).build()) }
        fun default(string: String) = apply { checkType(TfString(string)); blockBuilder.addElement(defaultBuilder.value(string).build()) }
        fun default(list: TfList) = apply { checkType(list); blockBuilder.addElement(defaultBuilder.value(list).build()) }
        fun default(map: TfMap) = apply { checkType(map); blockBuilder.addElement(defaultBuilder.value(map).build()) }
        fun sensitive() = apply { blockBuilder.addElement(sensitiveBuilder.value(true).build()) }
        fun build() = InputVariable<T>(
            blockBuilder.type(VARIABLE).addLabel(_name.toString()).build(),
            _name.toString()
        )

        private fun checkType(expression: Expression) {
            if (!expClass.isAssignableFrom(expression.javaClass)) {
                throw IllegalArgumentException("$expression is not from class ${expClass.name}")
            }
        }

        private fun checkAtInit(expressions: List<Expression>) {
            if (expressions.none { expClass.isAssignableFrom(it.javaClass) }) {
                throw IllegalArgumentException("${expClass.name} can't be created from any of the classes of $expressions")
            }
        }

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    override fun toString(): String = block.toString()
}
