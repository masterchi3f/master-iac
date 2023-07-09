package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier

class TfMap private constructor(
    val entries: Map<Identifier, Expression>
): Expression {
    private var additionalSpaces: Int = 0

    class Builder {
        private var _entries: Map<Identifier, Expression> = mutableMapOf()

        fun put(key: String, bool: Boolean) = apply { _entries = _entries + Pair(Identifier(key), TfBool(bool)) }
        fun put(key: String, number: Double) = apply { _entries = _entries + Pair(Identifier(key), TfNumber(number)) }
        fun put(key: String, string: String) = apply { _entries = _entries + Pair(Identifier(key), TfString(string)) }
        fun put(key: String, list: TfList) = apply { _entries = _entries + Pair(Identifier(key), list) }
        fun put(key: String, map: TfMap) = apply { _entries = _entries + Pair(Identifier(key), map) }
        fun put(key: String, ref: Reference<out Expression>) = apply { _entries = _entries + Pair(Identifier(key), ref) }
        fun put(key: String, file: TfFile) = apply { _entries = _entries + Pair(Identifier(key), file) }
        fun build() = TfMap(_entries)
    }

    fun setAdditionalSpaces(spaces: Int) = apply { additionalSpaces = spaces }

    override fun toString(): String {
        var str = "{${System.lineSeparator()}"
        var longestKey = 0
        entries.forEach {
            val length = it.key.toString().length
            if (length > longestKey) {
                longestKey = length
            }
        }
        entries.forEach {
            val spaces: Int = longestKey - it.key.toString().length
            str += "  ${" ".repeat(additionalSpaces)}${it.key}"
            str += " ".repeat(spaces)
            when (it.value) {
                is TfList -> (it.value as TfList).setAdditionalSpaces(additionalSpaces + 2)
                is TfMap -> (it.value as TfMap).setAdditionalSpaces(additionalSpaces + 2)
            }
            str += " = ${it.value}${System.lineSeparator()}"
        }
        str += "${" ".repeat(additionalSpaces)}}"
        return str
    }
}
