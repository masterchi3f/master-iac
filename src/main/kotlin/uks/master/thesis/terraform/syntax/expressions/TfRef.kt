package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfRef<T : Expression>(
    val reference: String
): Expression {
    override fun toString(): String = reference
}
