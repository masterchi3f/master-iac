package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

/**
 * This class is used for every value of an argument that this framework can't express
 * Should only be used in edge cases!
 */
class Raw(
    private val raw: String
): Expression {
    override fun toString(): String {
        return raw
    }
}
