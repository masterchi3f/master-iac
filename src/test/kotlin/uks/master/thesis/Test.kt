package uks.master.thesis

import kotlin.test.assertEquals
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.Executor
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.MultiLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineSymbol
import uks.master.thesis.terraform.syntax.elements.Resource
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap

class Test {

    private val logger = KotlinLogging.logger {}

    @Test
    fun test() {

        logger.info(
            Resource.Builder()
                .addElement(
                    Argument.Builder()
                        .value(TfMap.Builder().put("list", TfList.Builder().add(true).add(2.3).build()).build())
                        .name("a")
                        .comment(OneLineSymbol.DOUBLE_SLASH, "haha")
                        .build()
                )
                .addElement(
                    MultiLineComment.Builder().addElement(
                        Argument.Builder()
                            .value(TfMap.Builder().put("list", TfList.Builder().add(true).add(2.3).build()).build())
                            .name("a")
                            .comment(OneLineSymbol.DOUBLE_SLASH, "haha")
                            .build()
                    ).build()
                )
                .type("type")
                .name("name")
                .build()
                .toString()
        )
        Executor.downloadTerraformBinary(
            "https://releases.hashicorp.com/terraform/1.3.3/terraform_1.3.3_windows_386.zip"
        )
        assertEquals("terraform.exe", Executor.terraformCommand)
    }
    // https://github.com/lucasheld/wireguard-high-availability
    // https://developer.hashicorp.com/terraform/downloads
}
