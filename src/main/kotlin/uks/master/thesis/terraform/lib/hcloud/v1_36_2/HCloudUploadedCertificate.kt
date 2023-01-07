package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudUploadedCertificate {
    private const val HCLOUD_UPLOADED_CERTIFICATE: String = "hcloud_uploaded_certificate"
    private const val NAME: String = "name"
    private const val PRIVATE_KEY: String = "private_key"
    private const val CERTIFICATE: String = "certificate"
    private const val LABELS: String = "labels"
    private const val ID: String = "id"
    private const val DOMAIN_NAMES: String = "domain_names"
    private const val FINGERPRINT: String = "fingerprint"
    private const val CREATED: String = "created"
    private const val NOT_VALID_BEFORE: String = "not_valid_before"
    private const val NOT_VALID_AFTER: String = "not_valid_after"

    open class Resource protected constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(referenceString(ID))
        val name get() = TfRef<TfString>(referenceString(NAME))
        val certificate get() = TfRef<TfString>(referenceString(CERTIFICATE))
        val labels get() = TfRef<TfMap>(referenceString(LABELS))
        val domainNames get() = TfRef<TfList>(referenceString(DOMAIN_NAMES))
        val fingerprint get() = TfRef<TfString>(referenceString(FINGERPRINT))
        val created get() = TfRef<TfString>(referenceString(CREATED))
        val notValidBefore get() = TfRef<TfString>(referenceString(NOT_VALID_BEFORE))
        val notValidAfter get() = TfRef<TfString>(referenceString(NOT_VALID_AFTER))

        open class Builder(protected val isInherited: Boolean = true): GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val privateKeyBuilder: Argument.Builder = Argument.Builder().name(PRIVATE_KEY)
            private val certificateBuilder: Argument.Builder = Argument.Builder().name(CERTIFICATE)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            init { if (isInherited) resourceType(HCLOUD_UPLOADED_CERTIFICATE) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: TfRef<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun privateKey(privateKey: String) = apply { privateKeyBuilder.value(privateKey) }
            fun privateKey(ref: TfRef<TfString>) = apply { privateKeyBuilder.raw(ref.toString()) }
            fun certificate(certificate: String) = apply { certificateBuilder.value(certificate) }
            fun certificate(ref: TfRef<TfString>) = apply { certificateBuilder.raw(ref.toString()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(nameBuilder.build())
                addElement(privateKeyBuilder.build())
                addElement(certificateBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }
}
