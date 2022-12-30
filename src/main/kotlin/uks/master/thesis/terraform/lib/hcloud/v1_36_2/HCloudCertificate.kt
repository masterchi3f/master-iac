package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Block

@Deprecated("Alias for hcloud_uploaded_certificate to remain backwards compatible. Deprecated.")
object HCloudCertificate {
    private const val HCLOUD_CERTIFICATE: String = "hcloud_certificate"

    class Resource private constructor(block: Block, self: String): HCloudUploadedCertificate.Resource(block, self) {
        class Builder: HCloudUploadedCertificate.Resource.Builder(isInherited = false) {
            init { resourceType(HCLOUD_CERTIFICATE) }
        }
    }
}
