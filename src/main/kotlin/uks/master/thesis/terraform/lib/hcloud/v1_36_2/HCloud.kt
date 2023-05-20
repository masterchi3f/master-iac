package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import mu.KLogger
import mu.KotlinLogging
import uks.master.thesis.terraform.RootModule
import uks.master.thesis.terraform.TfVars
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.blocks.InputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.Provider
import uks.master.thesis.terraform.syntax.elements.blocks.RequiredProviders
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString

object HCloud {
    private val logger: KLogger = KotlinLogging.logger {}

    private const val HCLOUD: String = "hcloud"
    private const val SOURCE: String = "hetznercloud/hcloud"
    private const val VERSION: String = "1.36.2"
    private const val TOKEN: String = "token"
    private const val ENDPOINT: String = "endpoint"
    private const val POLL_INTERVAL: String = "poll_interval"
    private const val HCLOUD_TOKEN: String = "hcloud_token"

    private val providerBuilder: Provider.Builder = Provider.Builder().name(HCLOUD)
    private val tokenBuilder: Argument.Builder = Argument.Builder().name(TOKEN)
    private val endpointBuilder: Argument.Builder = Argument.Builder().name(ENDPOINT)
    private val pollIntervalBuilder: Argument.Builder = Argument.Builder().name(POLL_INTERVAL)
    private val providerCopy: Provider = Provider.Builder().name(HCLOUD).build()
    val token: InputVariable<TfString> = InputVariable.Builder().name(HCLOUD_TOKEN).sensitive().build(TfString::class.java)
    var provider: Provider? = null
        private set
        get() {
            var prov: Provider? = field
            field ?: run {
                prov = providerBuilder.addConfig(tokenBuilder.value(token).build()).build()
                provider = prov
            }
            return prov
        }

    fun endpoint(endpoint: String) = apply { providerBuilder.addConfig(endpointBuilder.value(endpoint).build()) }
    fun endpoint(ref: Reference<TfString>) = apply { providerBuilder.addConfig(endpointBuilder.raw(ref.toString()).build()) }
    fun pollInterval(pollInterval: String) = apply { providerBuilder.addConfig(pollIntervalBuilder.value(pollInterval).build()) }
    fun pollInterval(ref: Reference<TfString>) = apply { providerBuilder.addConfig(pollIntervalBuilder.raw(ref.toString()).build()) }

    fun requiredProviders(version: String = VERSION): RequiredProviders {
        if (version != VERSION) {
            logger.warn(
                "Library was build for hcloud version $VERSION. Be careful when using $version, " +
                "because Terraform elements may be changed. See latest version and latest elements at: " +
                    "https://registry.terraform.io/providers/hetznercloud/hcloud/latest/docs"
            )
        }
        return RequiredProviders.Builder().addProvider(providerCopy, SOURCE, version).build()
    }

    fun addProviderToRootModule(): HCloud = apply {
        provider?.let {
            RootModule.addProvider(it)
        }
    }

    fun addTokenToTfVarsFromEnv(key: String = HCLOUD_TOKEN): HCloud = apply {
        TfVars.addEnv(key, HCLOUD_TOKEN)
    }
}
