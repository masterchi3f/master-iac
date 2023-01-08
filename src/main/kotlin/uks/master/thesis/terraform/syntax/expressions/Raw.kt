package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class Raw(
    val raw: String
): Expression {
    override fun toString(): String = raw
}
