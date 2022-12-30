package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudManagedCertificate {
    private const val HCLOUD_MANAGED_CERTIFICATE: String = "hcloud_managed_certificate"
    private const val NAME: String = "name"
    private const val DOMAINS: String = "domains"
    private const val LABELS: String = "labels"
    private const val ID: String = "id"
    private const val CERTIFICATE: String = "certificate"
    private const val DOMAIN_NAMES: String = "domain_names"
    private const val FINGERPRINT: String = "fingerprint"
    private const val CREATED: String = "created"
    private const val NOT_VALID_BEFORE: String = "not_valid_before"
    private const val NOT_VALID_AFTER: String = "not_valid_after"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
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
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val domainsBuilder: Argument.Builder = Argument.Builder().name(DOMAINS)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            init { resourceType(HCLOUD_MANAGED_CERTIFICATE) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: TfRef<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun domains(domains: TfList) = apply { domainsBuilder.value(domains) }
            fun domains(ref: TfRef<TfList>) = apply { domainsBuilder.raw(ref.toString()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(nameBuilder.build())
                addElement(domainsBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
