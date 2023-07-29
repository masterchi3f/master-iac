package uks.master.thesis.interview

import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudServer
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.elements.blocks.InputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.Resource
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfString

/**
 * Expert interview resource example
 */
class CResource {
    private val logger: KLogger = KotlinLogging.logger {}

    @Test
    fun resource() {
        // -----------------Difference----------------- //
        val hCloudServer: Resource = Resource.Builder()
            .resourceType("hcloud_server")
            .resourceName("resNameTest")
        // -------------------------------------------- //
            .addElement(
                Argument.Builder()
                    .name("name")
                    .value("test")
                    .build()
            )
            .addElement(
                Argument.Builder()
                    .name("image")
                    .value("debian-11")
                    .build()
            )
            .addElement(
                Argument.Builder()
                    .name("server_type")
                    .value("cx11")
                    .build()
            )
            .addElement(
                Argument.Builder()
                    .name("location")
                    .value("nbg1")
                    .build()
            )
            .build()
        logger.info("\n" + hCloudServer)
    }

    @Test
    fun onlyBasis() {
        // -----------------Difference----------------- //
        val hCloudServer: Block = Block.Builder()
            .type("resource")
            .addLabel("hcloud_server")
            .addLabel("resNameTest")
        // -------------------------------------------- //
            .addElement(
                Argument.Builder()
                    .name("name")
                    .value("test")
                    .build()
            )
            .addElement(
                Argument.Builder()
                    .name("image")
                    .value("debian-11")
                    .build()
            )
            .addElement(
                Argument.Builder()
                    .name("server_type")
                    .value("cx11")
                    .build()
            )
            .addElement(
                Argument.Builder()
                    .name("location")
                    .value("nbg1")
                    .build()
            )
            .build()
        logger.info("\n" + hCloudServer)
    }

    @Test
    fun reference() {
        /**
         * Resource for reference
         */
        val res1: Resource = Resource.Builder()
            .resourceType("resType1")
            .resourceName("resName1")
            .addElement(
                Argument.Builder()
                    .name("someArgName")
                    .value(123.45)
                    .build()
            )
            .build()
        /**
         * Add references in this resource
         */
        val res2: Resource = Resource.Builder()
            .resourceType("resType2")
            .resourceName("resName2")
            /**
             * Reference to argument of another resource
             */
            .addElement(
                Argument.Builder()
                    .name("refToSomeArgName")
                    .value<TfNumber>(res1, "someArgName") // converts to Reference<TfNumber>("resType1.resName1.someArgName")
                    .build()
            )
            /**
             * Reference to another resource
             */
            .addElement(
                Argument.Builder()
                    .name("refToRes1")
                    .value<Raw>(res1) // converts to Reference<Raw>("resType1.resName1")
                    .build()
            )
            .build()
        logger.info("\n" + res2)
    }

    @Test
    fun short() {
        val hCloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resNameTest")
            .name("test")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .build()
        logger.info("\n" + hCloudServer)
    }

    @Test
    fun referenceAsBuilderArgument() {
        val image: InputVariable<TfString> = InputVariable.Builder()
            .name("image")
            .default("debian-11")
            .build(TfString::class.java)
        val hCloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resName1")
            .name("test1")
            .image(image.reference) // <---- HERE
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .build()
        val hCloudServer2: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resName2")
            .name("test2")
            .image(hCloudServer.image) // <---- HERE
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .build()
        logger.info("\n" + image)
        logger.info("\n" + hCloudServer)
        logger.info("\n" + hCloudServer2)
    }

    @Test
    fun referenceFromRes() {
        val hCloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resName1")
            .name("test1")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .build()
        logger.info("\n" + hCloudServer.reference)
        logger.info("\n" + hCloudServer.location)
        /**
         * What is constructed:
         * Reference<TfString>("hcloud_server.resName1.image")
         * What is used in HCloudServer.Resource:
         * Reference<TfString>(referenceString(IMAGE))
         */
        logger.info("\n" + hCloudServer.image)
    }
}
