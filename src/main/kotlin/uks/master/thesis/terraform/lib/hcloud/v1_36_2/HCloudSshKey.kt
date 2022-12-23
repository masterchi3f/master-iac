package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfMap

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
        val id get() = "${reference()}.$ID"
        val name get() = "${reference()}.$NAME"
        val publicKey get() = "${reference()}.$PUBLIC_KEY"
        val fingerprint get() = "${reference()}.$FINGERPRINT"
        val labels get() = "${reference()}.$LABELS"

        class Builder: uks.master.thesis.terraform.syntax.elements.blocks.Resource.GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val publicKeyBuilder: Argument.Builder = Argument.Builder().name(PUBLIC_KEY)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            init { resourceType(HCLOUD_SSH_KEY) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun nameRef(ref: String) = apply { nameBuilder.raw(ref) }
            fun publicKey(publicKey: String) = apply { publicKeyBuilder.value(publicKey) }
            fun publicKeyRef(ref: String) = apply { publicKeyBuilder.raw(ref) }
            fun labels(labels: TfMap) = apply { blockBuilder.addElement(labelsBuilder.value(labels).build()) }
            fun labelsRef(ref: String) = apply { blockBuilder.addElement(labelsBuilder.raw(ref).build()) }
            override fun build(): Resource {
                blockBuilder.addElement(nameBuilder.build()).addElement(publicKeyBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = "${reference()}.$ID"
        val name get() = "${reference()}.$NAME"
        val publicKey get() = "${reference()}.$PUBLIC_KEY"
        val fingerprint get() = "${reference()}.$FINGERPRINT"

        class Builder: uks.master.thesis.terraform.syntax.elements.blocks.DataSource.GBuilder<Builder>() {
            private val idBuilder: Argument.Builder = Argument.Builder().name(ID)
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val fingerprintBuilder: Argument.Builder = Argument.Builder().name(FINGERPRINT)
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            init { dataSource(HCLOUD_SSH_KEY) }

            fun id(id: String) = apply { blockBuilder.addElement(idBuilder.value(id).build()) }
            fun idRef(ref: String) = apply { blockBuilder.addElement(idBuilder.raw(ref).build()) }
            fun name(name: String) = apply { blockBuilder.addElement(nameBuilder.value(name).build()) }
            fun nameRef(ref: String) = apply { blockBuilder.addElement(nameBuilder.raw(ref).build()) }
            fun fingerprint(fingerprint: String) = apply { blockBuilder.addElement(fingerprintBuilder.value(fingerprint).build()) }
            fun fingerprintRef(ref: String) = apply { blockBuilder.addElement(fingerprintBuilder.raw(ref).build()) }
            fun withSelector(selector: String) = apply { blockBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelectorRef(ref: String) = apply { blockBuilder.addElement(withSelectorBuilder.raw(ref).build()) }
            override fun build(): DataSource = DataSource(buildBlock(), buildSelf())
        }
    }
}
