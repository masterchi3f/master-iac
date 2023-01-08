package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

/**
 * Expression which is only used with class Reference
 * when a resource or data source is directly addressed
 * without using one of its exported attributes
 */
class Raw(
    val raw: String
): Expression {
    override fun toString(): String = raw
}
