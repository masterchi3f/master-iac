package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier

open class Block(
    type: String,
    labels: List<String>,
    open val body: List<Element>
): Element {
    private var type: Identifier
    private var labels: List<Identifier>

    init {
        this.type = Identifier(type)
        this.labels = labels.map { label -> Identifier(label) }
    }

    override fun toString(): String {
        // Build labels
        var labelStr = ""
        labels.forEach { label: Identifier -> labelStr += "\"$label\" " }
        // Build body
        var bodyStr = ""
        body.forEach { element: Element -> bodyStr += "$element" }
        // Make pretty indentation
        bodyStr = "  " + bodyStr.replace(System.lineSeparator(), "${System.lineSeparator()}  ").dropLast(2)
        // Pack everything together
        return "$type " + labelStr + "{" + System.lineSeparator() + bodyStr + "}" + System.lineSeparator()
    }
}
