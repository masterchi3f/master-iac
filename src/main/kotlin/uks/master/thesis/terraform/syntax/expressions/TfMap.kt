package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier

class TfMap(
    map: Map<String, Expression>
): Expression {
    var map: Map<Identifier, Expression>

    init {
        this.map = map.mapKeys { entry -> Identifier(entry.key) }
    }

    override fun toString(): String {
        return map.toString()
    }
}
