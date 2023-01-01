package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudCertificate {
    private const val HCLOUD_CERTIFICATE: String = "hcloud_certificate"
    private const val ID: String = "id"
    private const val NAME: String = "name"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val CERTIFICATE: String = "certificate"
    private const val LABELS: String = "labels"
    private const val DOMAIN_NAMES: String = "domain_names"
    private const val FINGERPRINT: String = "fingerprint"
    private const val CREATED: String = "created"
    private const val NOT_VALID_BEFORE: String = "not_valid_before"
    private const val NOT_VALID_AFTER: String = "not_valid_after"

    @Deprecated("Alias for hcloud_uploaded_certificate to remain backwards compatible. Deprecated.")
    class Resource private constructor(block: Block, self: String): HCloudUploadedCertificate.Resource(block, self) {
        class Builder: HCloudUploadedCertificate.Resource.Builder(isInherited = false) {
            init { resourceType(HCLOUD_CERTIFICATE) }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val certificate get() = TfRef<TfString>(reference(CERTIFICATE))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val domainNames get() = TfRef<TfList>(reference(DOMAIN_NAMES))
        val fingerprint get() = TfRef<TfString>(reference(FINGERPRINT))
        val created get() = TfRef<TfString>(reference(CREATED))
        val notValidBefore get() = TfRef<TfString>(reference(NOT_VALID_BEFORE))
        val notValidAfter get() = TfRef<TfString>(reference(NOT_VALID_AFTER))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_CERTIFICATE) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: TfRef<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: TfRef<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
