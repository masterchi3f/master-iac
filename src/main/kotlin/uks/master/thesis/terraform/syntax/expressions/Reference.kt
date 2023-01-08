package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class Reference<T : Expression>(
    val reference: String
): Expression {
    override fun toString(): String = reference
}
