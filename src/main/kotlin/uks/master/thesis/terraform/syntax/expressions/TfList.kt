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
        fun add(ref: TfRef<out Expression>) = apply { _expressions = _expressions + ref }
        fun add(file: TfFile) = apply { _expressions = _expressions + file }
        fun build() = TfList(_expressions)
    }

    override fun toString(): String {
        var str = "["
        if (isSizeMoreThanOne()) {
            str += System.lineSeparator()
        }
        expressions.forEachIndexed { index, it ->
            if (isSizeMoreThanOne()) {
                str += "  "
            }
            str += it
            if (index + 1 != expressions.size) {
                str += ","
            }
            if (isSizeMoreThanOne()) {
                str += System.lineSeparator()
            }
        }
        str += "]"
        return str
    }

    private fun isSizeMoreThanOne(): Boolean = expressions.size > 1
}
