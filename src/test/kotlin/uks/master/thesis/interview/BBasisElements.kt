package uks.master.thesis.interview

import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap

/**
 * Expert interview basis elements example
 */
class BBasisElements {
    private val logger: KLogger = KotlinLogging.logger {}

    @Test
    fun basis() {
        /**
         * Two arguments:
         * name1 = "stringValue"
         * name2 = 123.5
         */
        val stringArgument: Argument = Argument.Builder()
            .name("name1")
            .value("stringValue")
            .build()
        val numberArgument: Argument = Argument.Builder()
            .name("name2")
            .value(123.5)
            .build()
        /**
         * A block containing one argument:
         * type "label1" "label2" {
         *   name1 = "stringValue"
         * }
         */
        val block: Block = Block.Builder()
            .type("type")
            .addLabel("label1")
            .addLabel("label2")
            .addElement(stringArgument)
            .build()
        /**
         * One block to contain them all
         */
        val bigBlock: Block = Block.Builder()
            .type("bigType")
            .addElement(numberArgument)
            .addElement(block)
            .build()
        logger.info("\n" + bigBlock)
    }

    @Test
    fun list() {
        val tfList: TfList = TfList.Builder()
            .add("string")
            .add(12.4)
            .add(true)
            .build()
        val listArgument: Argument = Argument.Builder()
            .name("listName")
            .value(tfList)
            .build()
        val block: Block = Block.Builder()
            .type("type")
            .addElement(listArgument)
            .build()
        logger.info("\n" + block)
    }

    @Test
    fun map() {
        val tfMap: TfMap = TfMap.Builder()
            .put("key1", "string")
            .put("key2", 12.4)
            .put("key3", true)
            .build()
        val listArgument: Argument = Argument.Builder()
            .name("mapName")
            .value(tfMap)
            .build()
        val block: Block = Block.Builder()
            .type("type")
            .addElement(listArgument)
            .build()
        logger.info("\n" + block)
    }
}
