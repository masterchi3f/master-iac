package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfFile
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudSshKey {
    private const val HCLOUD_SSH_KEY: String = "hcloud_ssh_key"
    private const val ID: String = "id"
    private const val NAME: String = "name"
    private const val PUBLIC_KEY: String = "public_key"
    private const val FINGERPRINT: String = "fingerprint"
    private const val LABELS: String = "labels"
    private const val WITH_SELECTOR: String = "with_selector"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val name get() = Reference<TfString>(referenceString(NAME))
        val publicKey get() = Reference<TfString>(referenceString(PUBLIC_KEY))
        val fingerprint get() = Reference<TfString>(referenceString(FINGERPRINT))
        val labels get() = Reference<TfMap>(referenceString(LABELS))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val publicKeyBuilder: Argument.Builder = Argument.Builder().name(PUBLIC_KEY)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            init { resourceType(HCLOUD_SSH_KEY) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: Reference<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun publicKey(publicKey: String) = apply { publicKeyBuilder.value(publicKey) }
            fun publicKey(ref: Reference<TfString>) = apply { publicKeyBuilder.raw(ref.toString()) }
            fun publicKey(file: TfFile) = apply { publicKeyBuilder.raw(file.toString()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: Reference<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(nameBuilder.build())
                addElement(publicKeyBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val name get() = Reference<TfString>(referenceString(NAME))
        val publicKey get() = Reference<TfString>(referenceString(PUBLIC_KEY))
        val fingerprint get() = Reference<TfString>(referenceString(FINGERPRINT))

        class Builder: GBuilder<Builder>() {
            private val idBuilder: Argument.Builder = Argument.Builder().name(ID)
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val fingerprintBuilder: Argument.Builder = Argument.Builder().name(FINGERPRINT)
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_SSH_KEY) }

            fun id(id: Int) = apply { addElement(idBuilder.value(id.toDouble()).build()) }
            fun id(ref: Reference<TfNumber>) = apply { addElement(idBuilder.raw(ref.toString()).build()) }
            fun name(name: String) = apply { addElement(nameBuilder.value(name).build()) }
            fun name(ref: Reference<TfString>) = apply { addElement(nameBuilder.raw(ref.toString()).build()) }
            fun fingerprint(fingerprint: String) = apply { addElement(fingerprintBuilder.value(fingerprint).build()) }
            fun fingerprint(ref: Reference<TfString>) = apply { addElement(fingerprintBuilder.raw(ref.toString()).build()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource = DataSource(buildBlock(), buildSelf())
        }
    }
}
