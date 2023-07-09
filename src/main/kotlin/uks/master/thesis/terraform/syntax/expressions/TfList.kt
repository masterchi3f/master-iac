package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfList private constructor(
    val expressions: List<Expression>
): Expression {
    private var additionalSpaces: Int = 0

    class Builder {
        private var _expressions: List<Expression> = mutableListOf()

        fun add(bool: Boolean) = apply { _expressions = _expressions + TfBool(bool) }
        fun add(number: Double) = apply { _expressions = _expressions + TfNumber(number) }
        fun add(string: String) = apply { _expressions = _expressions + TfString(string) }
        fun add(list: TfList) = apply { _expressions = _expressions + list }
        fun add(map: TfMap) = apply { _expressions = _expressions + map }
        fun add(ref: Reference<out Expression>) = apply { _expressions = _expressions + ref }
        fun add(file: TfFile) = apply { _expressions = _expressions + file }
        fun build() = TfList(_expressions)
    }

    fun setAdditionalSpaces(spaces: Int) = apply { additionalSpaces = spaces }

    override fun toString(): String {
        var str = "["
        if (isSizeMoreThanOne()) {
            str += System.lineSeparator()
        }
        expressions.forEachIndexed { index, it ->
            when (it) {
                is TfList -> it.setAdditionalSpaces(additionalSpaces + 2)
                is TfMap -> it.setAdditionalSpaces(additionalSpaces + 2)
            }
            if (isSizeMoreThanOne()) {
                str += "  "
            }
            str += "${" ".repeat(additionalSpaces)}$it"
            if (index + 1 != expressions.size) {
                str += ","
            }
            if (isSizeMoreThanOne()) {
                str += System.lineSeparator()
            }
        }
        str += "${" ".repeat(additionalSpaces)}]"
        return str
    }

    private fun isSizeMoreThanOne(): Boolean = expressions.size > 1
        || expressions.size == 1 && (expressions[0] is TfList || expressions[0] is TfMap)
}
