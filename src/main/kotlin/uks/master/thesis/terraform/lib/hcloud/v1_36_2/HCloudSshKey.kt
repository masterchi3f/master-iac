package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.blocks.DataSource
import uks.master.thesis.terraform.syntax.elements.blocks.Resource
import uks.master.thesis.terraform.syntax.expressions.TfMap

object HCloudSshKey {
    private const val HCLOUD_SSH_KEY: String = "hcloud_ssh_key"
    private const val ID: String = "id"
    private const val NAME: String = "name"
    private const val PUBLIC_KEY: String = "public_key"
    private const val FINGERPRINT: String = "fingerprint"
    private const val LABELS: String = "labels"
    private const val WITH_SELECTOR: String = "with_selector"

    class _Resource private constructor(private val res: Resource) {
        val id get() = "${res.reference()}.$ID"
        val name get() = "${res.reference()}.$NAME"
        val publicKey get() = "${res.reference()}.$PUBLIC_KEY"
        val fingerprint get() = "${res.reference()}.$FINGERPRINT"
        val labels get() = "${res.reference()}.$LABELS"

        class Builder {
            private val resourceBuilder: Resource.Builder = Resource.Builder()
            private lateinit var _resName: Identifier
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val publicKeyBuilder: Argument.Builder = Argument.Builder().name(PUBLIC_KEY)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)

            fun resName(resName: String) = apply { preventDupResName(); _resName = Identifier(resName) }
            fun name(name: String) = apply { nameBuilder.value(name) }
            fun nameRef(ref: String) = apply { nameBuilder.raw(ref) }
            fun publicKey(publicKey: String) = apply { publicKeyBuilder.value(publicKey) }
            fun publicKeyRef(ref: String) = apply { publicKeyBuilder.raw(ref) }
            fun labels(labels: TfMap) = apply { resourceBuilder.addElement(labelsBuilder.value(labels).build()) }
            fun labelsRef(ref: String) = apply { resourceBuilder.addElement(labelsBuilder.raw(ref).build()) }
            fun build() = _Resource(
                resourceBuilder
                    .type(HCLOUD_SSH_KEY)
                    .name(_resName.toString())
                    .addElement(nameBuilder.build())
                    .addElement(publicKeyBuilder.build())
                    .build()
            )

            private fun preventDupResName() {
                if (::_resName.isInitialized) throw IllegalArgumentException("Resource name was already set to $_resName!")
            }
        }

        override fun toString(): String = res.toString()
    }

    class _DataSource private constructor(private val data: DataSource) {
        val id get() = "${data.reference()}.$ID"
        val name get() = "${data.reference()}.$NAME"
        val publicKey get() = "${data.reference()}.$PUBLIC_KEY"
        val fingerprint get() = "${data.reference()}.$FINGERPRINT"

        class Builder {
            private val datasourceBuilder: DataSource.Builder = DataSource.Builder()
            private lateinit var _dataName: Identifier
            private val idBuilder: Argument.Builder = Argument.Builder().name(ID)
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val fingerprintBuilder: Argument.Builder = Argument.Builder().name(FINGERPRINT)
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)

            fun dataName(dataName: String) = apply { preventDupDataName(); _dataName = Identifier(dataName) }
            fun id(id: String) = apply { datasourceBuilder.addElement(idBuilder.value(id).build()) }
            fun idRef(ref: String) = apply { datasourceBuilder.addElement(idBuilder.raw(ref).build()) }
            fun name(name: String) = apply { datasourceBuilder.addElement(nameBuilder.value(name).build()) }
            fun nameRef(ref: String) = apply { datasourceBuilder.addElement(nameBuilder.raw(ref).build()) }
            fun fingerprint(fingerprint: String) = apply { datasourceBuilder.addElement(fingerprintBuilder.value(fingerprint).build()) }
            fun fingerprintRef(ref: String) = apply { datasourceBuilder.addElement(fingerprintBuilder.raw(ref).build()) }
            fun withSelector(selector: String) = apply { datasourceBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelectorRef(ref: String) = apply { datasourceBuilder.addElement(withSelectorBuilder.raw(ref).build()) }
            fun build() = _DataSource(
                datasourceBuilder.source(HCLOUD_SSH_KEY).name(_dataName.toString()).build()
            )

            private fun preventDupDataName() {
                if (::_dataName.isInitialized) throw IllegalArgumentException("Data source name was already set to $_dataName!")
            }
        }

        override fun toString(): String = data.toString()
    }
}
