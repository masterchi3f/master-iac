package uks.master.thesis.terraform.syntax.utils

object Utils {
    fun convertToIdentifier(reference: String): String =
        reference.replace(".", "_")
}
