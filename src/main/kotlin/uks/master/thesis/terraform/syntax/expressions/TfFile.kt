package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfFile(
    private val path: String
): Expression {
    override fun toString(): String = "file(\"$path\")"
}
