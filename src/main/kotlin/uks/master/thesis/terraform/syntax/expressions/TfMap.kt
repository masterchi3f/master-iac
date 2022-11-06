package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier

class TfMap private constructor(
    private val entries: Map<Identifier, Expression>
): Expression {
    class Builder {
        private var _entries: Map<Identifier, Expression> = mutableMapOf();

        fun put(key: String, bool: Boolean) = apply { _entries = _entries + Pair(Identifier(key), TfBool(bool)) }
        fun put(key: String, number: Double) = apply { _entries = _entries + Pair(Identifier(key), TfNumber(number)) }
        fun put(key: String, string: String) = apply { _entries = _entries + Pair(Identifier(key), TfString(string)) }
        fun put(key: String, list: TfList) = apply { _entries = _entries + Pair(Identifier(key), list) }
        fun put(key: String, map: TfMap) = apply { _entries = _entries + Pair(Identifier(key), map) }
        fun build() = TfMap(_entries)
    }

    override fun toString(): String {
        return entries.toString()
    }
}
