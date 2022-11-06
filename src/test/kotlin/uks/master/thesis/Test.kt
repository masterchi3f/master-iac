package uks.master.thesis

import kotlin.test.assertEquals
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.Executor
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.MultiLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineSymbol
import uks.master.thesis.terraform.syntax.elements.Resource
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNull
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfString

class Test {

    private val logger = KotlinLogging.logger {}

    @Test
    fun test() {
//        logger.info(
//            .toString()
//        )
        logger.info(Resource("a", "b", listOf(
            Argument("a", TfBool(true)),
            Argument("b", TfNumber(5.87)),
            OneLineComment(OneLineSymbol.DOUBLE_SLASH, "testabe jhuduh"),
            Argument("c", TfString("ccc")),
            Resource("a", "b", listOf(
                Argument("a", TfNull()),
                Argument("b", TfMap(mapOf<String, Expression>(Pair("hi-_k", TfString("aaa")), Pair("h-_k", TfString("aaa"))))),
                OneLineComment(OneLineSymbol.DOUBLE_SLASH, "testabe jhuduh"),
                Argument("c", TfString("ccc"))
            )),
            MultiLineComment(listOf(
                Resource("a", "b", listOf(
                    Argument("a", TfString("aaa")),
                    Argument("b", TfString("bbb")),
                    Argument("c", TfString("ccc"))
                )),
                Argument("b", TfList(listOf(TfString("bbb"), TfNumber(4.3)))),
                Argument("c", TfString("ccc")),
                OneLineComment(OneLineSymbol.DOUBLE_SLASH, "testabe jhuduh")
            ))
        )).toString())
        Executor.downloadTerraformBinary(
            "https://releases.hashicorp.com/terraform/1.3.3/terraform_1.3.3_windows_386.zip"
        )
        assertEquals("terraform.exe", Executor.terraformCommand)
    }
    // https://github.com/lucasheld/wireguard-high-availability
    // https://developer.hashicorp.com/terraform/downloads
}
