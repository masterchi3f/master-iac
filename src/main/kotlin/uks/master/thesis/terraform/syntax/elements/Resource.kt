package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.syntax.Element

data class Resource(
    private val type: String,
    private val name: String,
    override val body: List<Element>
): Block(
    RESOURCE,
    listOf(type, name),
    body
) {
    companion object {
        const val RESOURCE: String = "resource"
    }

    override fun toString(): String {
        return super.toString()
    }
}
