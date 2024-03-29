package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier

class Block private constructor(
    private val type: Identifier,
    private val labels: List<Identifier>,
    private val elements: List<Element>
): Element, Child {
    class Builder {
        private lateinit var _type: Identifier
        private var _labels: List<Identifier> = mutableListOf()
        private var _elements: List<Element> = mutableListOf()

        fun type(type: String) = apply { preventDupType(); _type = Identifier(type) }
        fun addLabel(label: String) = apply { _labels = _labels + Identifier(label) }
        fun addElement(block: Block) = apply { _elements = _elements + block }
        fun addElement(argument: Argument) = apply { _elements = _elements + argument }
        fun addElement(symbol: OneLineSymbol, text: String) = apply { _elements = _elements + OneLineComment(symbol, text) }
        fun addElement(multiLineComment: MultiLineComment) = apply { _elements = _elements + multiLineComment }
        fun build() = Block(_type, _labels, _elements)

        private fun preventDupType() {
            require(!::_type.isInitialized) {"type was already set to $_type!"}
        }
    }

    override fun toString(): String {
        // Build labels
        var labelStr = ""
        labels.forEach { label: Identifier -> labelStr += "\"$label\" " }
        // Build body
        var bodyStr = ""
        setAdditionalSpacesForArguments()
        elements.forEach { element: Element -> bodyStr += element }
        // Make pretty indentation
        if (bodyStr != "") {
            bodyStr = "  " + bodyStr.replace(System.lineSeparator(), "${System.lineSeparator()}  ").dropLast(2)
        }
        // Pack everything together
        return "$type " + labelStr + "{" + System.lineSeparator() + bodyStr + "}" + System.lineSeparator()
    }

    private fun setAdditionalSpacesForArguments() {
        var longestName = 0
        elements.forEach {
            if (it is Argument && it.nameSize() > longestName) {
                longestName = it.nameSize()
            }
        }
        elements.forEach {
            if (it is Argument) {
                it.setAdditionalSpaces(longestName - it.nameSize())
            }
        }
    }
}
