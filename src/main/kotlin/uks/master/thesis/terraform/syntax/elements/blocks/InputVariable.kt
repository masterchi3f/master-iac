package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfFile
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
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

    val reference get() = Reference<T>("$VAR.$self")
    val name get() = self

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val defaultBuilder: Argument.Builder = Argument.Builder().name(DEFAULT)
        private val sensitiveBuilder: Argument.Builder = Argument.Builder().name(SENSITIVE)
        private lateinit var _name: Identifier
        private var default: Expression? = null

        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun default(bool: Boolean) = apply { default = TfBool(bool); blockBuilder.addElement(defaultBuilder.value(bool).build()) }
        fun default(number: Double) = apply { default = TfNumber(number); blockBuilder.addElement(defaultBuilder.value(number).build()) }
        fun default(string: String) = apply { default = TfString(string); blockBuilder.addElement(defaultBuilder.value(string).build()) }
        fun default(list: TfList) = apply { default = list; checkList(list); blockBuilder.addElement(defaultBuilder.value(list).build()) }
        fun default(map: TfMap) = apply { default = map; checkMap(map); blockBuilder.addElement(defaultBuilder.value(map).build()) }
        fun sensitive() = apply { blockBuilder.addElement(sensitiveBuilder.value(true).build()) }
        fun <S: Expression>build(type: Class<S>): InputVariable<S> {
            default?.let {
                if (!type.isAssignableFrom(it.javaClass)) {
                    throw IllegalArgumentException("$default is not from class ${type.name}")
                }
            }
            if (type.isAssignableFrom(TfBool::class.java)
                || type.isAssignableFrom(TfNumber::class.java)
                || type.isAssignableFrom(TfString::class.java)
                || type.isAssignableFrom(TfList::class.java)
                || type.isAssignableFrom(TfMap::class.java)
                || type.isAssignableFrom(Raw::class.java)
            ) {
                return InputVariable(blockBuilder.type(VARIABLE).addLabel(_name.toString()).build(), _name.toString())
            }
            throw IllegalArgumentException("${type.name} must be ${TfBool::class.simpleName}, ${TfNumber::class.simpleName}, " +
                "${TfString::class.simpleName}, ${TfList::class.simpleName}, ${TfMap::class.simpleName} or ${Raw::class.simpleName}")
        }

        private fun checkList(list: TfList) {
            list.expressions.forEach {
                when (it) {
                    is Reference<out Expression>, is TfFile -> throw IllegalArgumentException("Only primitive types are allowed in default values")
                    is TfList -> checkList(it)
                    is TfMap -> checkMap(it)
                }
            }
        }

        private fun checkMap(map: TfMap) {
            map.entries.forEach {
                when (val value = it.value) {
                    is Reference<out Expression>, is TfFile -> throw IllegalArgumentException("Only primitive types are allowed in default values")
                    is TfList -> checkList(value)
                    is TfMap -> checkMap(value)
                }
            }
        }

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    override fun toString(): String = block.toString()
}
