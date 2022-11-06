package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNull
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfString

class Argument private constructor(
    private val name: Identifier,
    private val value: Expression,
    private val comment: OneLineComment?
): Element {
    class Builder {
        private lateinit var _name: Identifier
        private lateinit var _value: Expression
        private var _comment: OneLineComment? = null

        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun nullValue() = apply { preventDupValue(); _value = TfNull() }
        fun value(bool: Boolean) = apply { preventDupValue(); _value = TfBool(bool); }
        fun value(number: Double) = apply { preventDupValue(); _value = TfNumber(number) }
        fun value(string: String) = apply { preventDupValue(); _value = TfString(string) }
        fun value(list: TfList) = apply { preventDupValue(); _value = list }
        fun value(map: TfMap) = apply { preventDupValue(); _value = map }
        fun comment(symbol: OneLineSymbol, text: String) = apply { preventDupComment(); _comment = OneLineComment(symbol, text) }
        fun build() = Argument(_name, _value, _comment)

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }

        private fun preventDupValue() {
            if (::_value.isInitialized) throw IllegalArgumentException("value was already set to $_value!")
        }

        private fun preventDupComment() {
            _comment?.let { throw IllegalArgumentException("comment was already set to $it!") }
        }
    }

    override fun toString(): String {
        return "$name = $value${comment?.let { " $it" } ?: ""}${System.lineSeparator()}"
    }
}
