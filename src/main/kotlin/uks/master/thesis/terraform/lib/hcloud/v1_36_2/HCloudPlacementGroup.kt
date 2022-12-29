package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloudPlacementGroup {
    private const val HCLOUD_PLACEMENT_GROUP: String = "hcloud_placement_group"
    private const val NAME: String = "name"
    private const val TYPE: String = "type"
    private const val LABELS: String = "labels"
    private const val ID: String = "id"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val MOST_RECENT: String = "most_recent"

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val type get() = TfRef<TfString>(reference(TYPE))
        val labels get() = TfRef<TfMap>(reference(LABELS))

        class Builder: GBuilder<Builder>() {
            private val typeBuilder: Argument.Builder = Argument.Builder().name(TYPE)
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            init { resourceType(HCLOUD_PLACEMENT_GROUP) }

            fun type(type: String) = apply { typeBuilder.value(type) }
            fun type(ref: TfRef<TfString>) = apply { typeBuilder.raw(ref.toString()) }
            fun name(name: String) = apply { blockBuilder.addElement(nameBuilder.value(name).build()) }
            fun name(ref: TfRef<TfString>) = apply { blockBuilder.addElement(nameBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { blockBuilder.addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { blockBuilder.addElement(labelsBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                blockBuilder.addElement(typeBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }
        }
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val type get() = TfRef<TfString>(reference(TYPE))
        val labels get() = TfRef<TfMap>(reference(LABELS))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val mostRecentBuilder: Argument.Builder = Argument.Builder().name(MOST_RECENT)
            init { dataSource(HCLOUD_PLACEMENT_GROUP) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: TfRef<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: TfRef<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { blockBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { blockBuilder.addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun mostRecent(mostRecent: Boolean) = apply { blockBuilder.addElement(mostRecentBuilder.value(mostRecent).build()) }
            fun mostRecent(ref: TfRef<TfBool>) = apply { blockBuilder.addElement(mostRecentBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                blockBuilder.addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
