package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfList private constructor(
    private val expressions: List<Expression>
): Expression {
    class Builder {
        private var _expressions: List<Expression> = mutableListOf()

        fun add(bool: Boolean) = apply { _expressions = _expressions + TfBool(bool) }
        fun add(number: Double) = apply { _expressions = _expressions + TfNumber(number) }
        fun add(string: String) = apply { _expressions = _expressions + TfString(string) }
        fun add(list: TfList) = apply { _expressions = _expressions + list }
        fun add(map: TfMap) = apply { _expressions = _expressions + map }
        fun add(ref: TfRef) = apply { _expressions = _expressions + ref }
        fun build() = TfList(_expressions)
    }

    override fun toString(): String {
        return expressions.toString()
    }
}
