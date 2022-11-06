package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

data class TfList(
    val list: List<Expression>
): Expression {
    override fun toString(): String {
        return list.toString()
    }
}
