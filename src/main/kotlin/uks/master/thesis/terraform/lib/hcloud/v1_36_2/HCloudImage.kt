package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudImage {
    private const val HCLOUD_IMAGE: String = "hcloud_image"
    private const val ID: String = "id"
    private const val NAME: String = "name"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val MOST_RECENT: String = "most_recent"
    private const val WITH_STATUS: String = "with_status"
    private const val TYPE: String = "type"
    private const val STATUS: String = "status"
    private const val DESCRIPTION: String = "description"
    private const val CREATED: String = "created"
    private const val OS_FLAVOR: String = "os_flavor"
    private const val OS_VERSION: String = "os_version"
    private const val RAPID_DEPLOY: String = "rapid_deploy"
    private const val DEPRECATED: String = "deprecated"

    enum class Status(private val status: String) {
        CREATING("creating"),
        AVAILABLE("available");
        override fun toString(): String = status
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(referenceString(ID))
        val name get() = TfRef<TfString>(referenceString(NAME))
        val type get() = TfRef<TfString>(referenceString(TYPE))
        val status get() = TfRef<TfString>(referenceString(STATUS))
        val description get() = TfRef<TfString>(referenceString(DESCRIPTION))
        val created get() = TfRef<TfString>(referenceString(CREATED))
        val osFlavor get() = TfRef<TfString>(referenceString(OS_FLAVOR))
        val osVersion get() = TfRef<TfString>(referenceString(OS_VERSION))
        val rapidDeploy get() = TfRef<TfBool>(referenceString(RAPID_DEPLOY))
        val deprecated get() = TfRef<TfString>(referenceString(DEPRECATED))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val mostRecentBuilder: Argument.Builder = Argument.Builder().name(MOST_RECENT)
            private val withStatusBuilder: Argument.Builder = Argument.Builder().name(WITH_STATUS)
            init { dataSource(HCLOUD_IMAGE) }

            fun id(id: Int) = apply { addElement(idOrNameBuilder.name(ID).value(id.toDouble()).build()) }
            fun id(ref: TfRef<TfNumber>) = apply { addElement(idOrNameBuilder.name(ID).raw(ref.toString()).build()) }
            fun name(name: String) = apply { addElement(idOrNameBuilder.name(NAME).value(name).build()) }
            fun name(ref: TfRef<TfString>) = apply { addElement(idOrNameBuilder.name(NAME).raw(ref.toString()).build()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun mostRecent(mostRecent: Boolean) = apply { addElement(mostRecentBuilder.value(mostRecent).build()) }
            fun mostRecent(ref: TfRef<TfBool>) = apply { addElement(mostRecentBuilder.raw(ref.toString()).build()) }
            fun withStatus(withStatus: List<Status>) = apply {
                val set: Set<Status> = withStatus.toSet()
                val tfListBuilder: TfList.Builder = TfList.Builder()
                set.forEach { tfListBuilder.add(it.toString()) }
                addElement(withStatusBuilder.value(tfListBuilder.build()).build())
            }
            override fun build(): DataSource = DataSource(buildBlock(), buildSelf())
        }
    }
}
