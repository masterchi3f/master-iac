package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfString
import uks.master.thesis.terraform.utils.Utils

object HCloudLoadBalancerService {
    private const val HCLOUD_LOAD_BALANCER_SERVICE: String = "hcloud_load_balancer_service"
    private const val LOAD_BALANCER_ID: String = "load_balancer_id"
    private const val PROTOCOL: String = "protocol"
    private const val LISTEN_PORT: String = "listen_port"
    private const val DESTINATION_PORT: String = "destination_port"
    private const val PROXYPROTOCL: String = "proxyprotocol"

    enum class Protocol(private val protocol: String) {
        HTTP("http"),
        HTTPS("https"),
        TCP("tcp");
        override fun toString(): String = protocol
    }

    class Http private constructor(val block: Block) {
        companion object {
            private const val HTTP: String = "http"
            private const val STICKY_SESSIONS: String = "sticky_sessions"
            private const val COOKIE_NAME: String = "cookie_name"
            private const val COOKIE_LIFETIME: String = "cookie_lifetime"
            private const val CERTIFICATES: String = "certificates"
            private const val REDIRECT_HTTP: String = "redirect_http"

            fun reference(index: Int? = null): String = Utils.splatExp(HTTP, index)
            fun cookieName(index: Int? = null): String = Utils.splatExp(HTTP, index, COOKIE_NAME)
            fun cookieLifetime(index: Int? = null): String = Utils.splatExp(HTTP, index, COOKIE_LIFETIME)
            fun certificates(index: Int? = null): String = Utils.splatExp(HTTP, index, CERTIFICATES)
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder().type(HTTP)
            private val stickySessionsBuilder: Argument.Builder = Argument.Builder().name(STICKY_SESSIONS)
            private val cookieNameBuilder: Argument.Builder = Argument.Builder().name(COOKIE_NAME)
            private val cookieLifetimeBuilder: Argument.Builder = Argument.Builder().name(COOKIE_LIFETIME)
            private val certificatesBuilder: Argument.Builder = Argument.Builder().name(CERTIFICATES)
            private val redirectHttpBuilder: Argument.Builder = Argument.Builder().name(REDIRECT_HTTP)
            private var isRedirectHttpSet: Boolean = false
            private var _protocol: Protocol? = null

            fun protocol(protocol: Protocol) = apply { preventDupProtocol(); _protocol = protocol }
            fun stickySessions(stickySessions: Boolean) = apply { blockBuilder.addElement(stickySessionsBuilder.value(stickySessions).build()) }
            fun stickySessions(ref: Reference<TfBool>) = apply { blockBuilder.addElement(stickySessionsBuilder.raw(ref.toString()).build()) }
            fun cookieName(cookieName: String) = apply { blockBuilder.addElement(cookieNameBuilder.value(cookieName).build()) }
            fun cookieName(ref: Reference<TfString>) = apply { blockBuilder.addElement(cookieNameBuilder.raw(ref.toString()).build()) }
            fun cookieLifetime(cookieLifetime: Int) = apply { blockBuilder.addElement(cookieLifetimeBuilder.value(cookieLifetime.toDouble()).build()) }
            fun cookieLifetime(ref: Reference<TfNumber>) = apply { blockBuilder.addElement(cookieLifetimeBuilder.raw(ref.toString()).build()) }
            fun certificates(certificates: TfList) = apply { blockBuilder.addElement(certificatesBuilder.value(certificates).build()) }
            fun certificates(ref: Reference<TfList>) = apply { blockBuilder.addElement(certificatesBuilder.raw(ref.toString()).build()) }
            fun redirectHttp(redirectHttp: Boolean) = apply { isRedirectHttpSet = true; redirectHttpBuilder.value(redirectHttp) }
            fun redirectHttp(ref: Reference<TfBool>) = apply { isRedirectHttpSet = true; redirectHttpBuilder.raw(ref.toString()) }
            fun build(): Http {
                if (isRedirectHttpSet) {
                    if (_protocol?.equals(Protocol.HTTPS) == false) {
                        throw IllegalArgumentException("HTTPS traffic may only be redirected to HTTPS when HTTPS protocol is set in parent element")
                    } else {
                        blockBuilder.addElement(redirectHttpBuilder.build())
                    }
                }
                return Http(blockBuilder.build())
            }

            private fun preventDupProtocol() {
                _protocol?.let { throw IllegalArgumentException("protocol was already set to $_protocol") }
            }
        }

        override fun toString(): String = block.toString()
    }

    class HealthCheck private constructor(val block: Block) {
        companion object {
            private const val HEALTH_CHECK: String = "health_check"
            private const val PROTOCOL: String = "protocol"
            private const val PORT: String = "port"
            private const val INTERVAL: String = "interval"
            private const val TIMEOUT: String = "timeout"
            private const val RETRIES: String = "retries"

            fun reference(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index)
            fun protocol(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, PROTOCOL)
            fun port(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, PORT)
            fun interval(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, INTERVAL)
            fun timeout(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, TIMEOUT)
            fun retries(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, RETRIES)
            fun httpList(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, Http.reference())
            fun httpListDomains(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, Http.domain())
            fun httpListPaths(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, Http.path())
            fun httpListResponses(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, Http.response())
            fun httpListTlsList(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, Http.tls())
            fun httpListStatusCodesList(index: Int? = null): String = Utils.splatExp(HEALTH_CHECK, index, Http.statusCodes())
            fun http(index: Int? = null, httpIndex: Int): String = Utils.splatExp(HEALTH_CHECK, index, Http.reference(httpIndex))
            fun httpListDomain(index: Int? = null, httpIndex: Int): String = Utils.splatExp(HEALTH_CHECK, index, Http.domain(httpIndex))
            fun httpListPath(index: Int? = null, httpIndex: Int): String = Utils.splatExp(HEALTH_CHECK, index, Http.path(httpIndex))
            fun httpListResponse(index: Int? = null, httpIndex: Int): String = Utils.splatExp(HEALTH_CHECK, index, Http.response(httpIndex))
            fun httpListTls(index: Int? = null, httpIndex: Int): String = Utils.splatExp(HEALTH_CHECK, index, Http.tls(httpIndex))
            fun httpListStatusCodes(index: Int? = null, httpIndex: Int): String = Utils.splatExp(HEALTH_CHECK, index, Http.statusCodes(httpIndex))
        }

        enum class Protocol(private val protocol: String) {
            HTTP("http"),
            TCP("tcp");
            override fun toString(): String = protocol
        }

        class Http private constructor(val block: Block) {
            companion object {
                private const val HTTP: String = "http"
                private const val DOMAIN: String = "domain"
                private const val PATH: String = "path"
                private const val RESPONSE: String = "response"
                private const val TLS: String = "tls"
                private const val STATUS_CODES: String = "status_codes"

                fun reference(index: Int? = null): String = Utils.splatExp(HTTP, index)
                fun domain(index: Int? = null): String = Utils.splatExp(HTTP, index, DOMAIN)
                fun path(index: Int? = null): String = Utils.splatExp(HTTP, index, PATH)
                fun response(index: Int? = null): String = Utils.splatExp(HTTP, index, RESPONSE)
                fun tls(index: Int? = null): String = Utils.splatExp(HTTP, index, TLS)
                fun statusCodes(index: Int? = null): String = Utils.splatExp(HTTP, index, STATUS_CODES)
            }

            class Builder {
                private val blockBuilder: Block.Builder = Block.Builder().type(HTTP)
                private val domainBuilder: Argument.Builder = Argument.Builder().name(DOMAIN)
                private val pathBuilder: Argument.Builder = Argument.Builder().name(PATH)
                private val responseBuilder: Argument.Builder = Argument.Builder().name(RESPONSE)
                private val tlsBuilder: Argument.Builder = Argument.Builder().name(TLS)
                private val statusCodesBuilder: Argument.Builder = Argument.Builder().name(STATUS_CODES)

                fun domain(domain: String) = apply { blockBuilder.addElement(domainBuilder.value(domain).build()) }
                fun domain(ref: Reference<TfString>) = apply { blockBuilder.addElement(domainBuilder.raw(ref.toString()).build()) }
                fun path(path: String) = apply { blockBuilder.addElement(pathBuilder.value(path).build()) }
                fun path(ref: Reference<TfString>) = apply { blockBuilder.addElement(pathBuilder.raw(ref.toString()).build()) }
                fun response(response: String) = apply { blockBuilder.addElement(responseBuilder.value(response).build()) }
                fun response(ref: Reference<TfString>) = apply { blockBuilder.addElement(responseBuilder.raw(ref.toString()).build()) }
                fun tls(tls: Boolean) = apply { blockBuilder.addElement(tlsBuilder.value(tls).build()) }
                fun tls(ref: Reference<TfBool>) = apply { blockBuilder.addElement(tlsBuilder.raw(ref.toString()).build()) }
                fun statusCodes(statusCodes: TfList) = apply { blockBuilder.addElement(statusCodesBuilder.value(statusCodes).build()) }
                fun statusCodes(ref: Reference<TfList>) = apply { blockBuilder.addElement(statusCodesBuilder.raw(ref.toString()).build()) }
                fun build() = Http(blockBuilder.build())
            }

            override fun toString(): String = block.toString()
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder().type(HEALTH_CHECK)
            private val protocolBuilder: Argument.Builder = Argument.Builder().name(PROTOCOL)
            private val portBuilder: Argument.Builder = Argument.Builder().name(PORT)
            private val intervalBuilder: Argument.Builder = Argument.Builder().name(INTERVAL)
            private val timeoutBuilder: Argument.Builder = Argument.Builder().name(TIMEOUT)
            private val retriesBuilder: Argument.Builder = Argument.Builder().name(RETRIES)
            private var httpList: List<Http> = mutableListOf()
            private lateinit var _protocol: Protocol

            fun protocol(protocol: Protocol) = apply { preventDupProtocol(); _protocol = protocol }
            fun port(port: Int) = apply { portBuilder.value(port.toDouble()) }
            fun port(ref: Reference<TfNumber>) = apply { portBuilder.raw(ref.toString()) }
            fun interval(interval: Int) = apply { intervalBuilder.value(interval.toDouble()) }
            fun interval(ref: Reference<TfNumber>) = apply { intervalBuilder.raw(ref.toString()) }
            fun timeout(timeout: Int) = apply { timeoutBuilder.value(timeout.toDouble()) }
            fun timeout(ref: Reference<TfNumber>) = apply { timeoutBuilder.raw(ref.toString()) }
            fun http(http: Http) = apply { httpList = httpList + http }
            fun retries(retries: Int) = apply { blockBuilder.addElement(retriesBuilder.value(retries.toDouble()).build()) }
            fun retries(ref: Reference<TfNumber>) = apply { blockBuilder.addElement(retriesBuilder.raw(ref.toString()).build()) }
            fun build(): HealthCheck {
                blockBuilder.addElement(protocolBuilder.value(_protocol.toString()).build())
                blockBuilder.addElement(intervalBuilder.build())
                blockBuilder.addElement(timeoutBuilder.build())
                if (_protocol == Protocol.TCP) {
                    blockBuilder.addElement(portBuilder.build())
                    httpList.forEach { blockBuilder.addElement(it.block) }
                }
                return HealthCheck(blockBuilder.build())
            }

            private fun preventDupProtocol() {
                if (::_protocol.isInitialized) throw IllegalArgumentException("protocol was already set to $_protocol!")
            }
        }

        override fun toString(): String = block.toString()
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val protocol get() = Reference<TfString>(referenceString(PROTOCOL))
        val listenPort get() = Reference<TfNumber>(referenceString(LISTEN_PORT))
        val destinationPort get() = Reference<TfNumber>(referenceString(DESTINATION_PORT))
        val proxyprotocol get() = Reference<TfBool>(referenceString(PROXYPROTOCL))
        val httpList get() = Reference<TfList>(referenceString(Http.reference()))
        val httpListCookieNames get() = Reference<TfList>(referenceString(Http.cookieName()))
        val httpListCookieLifetime get() = Reference<TfList>(referenceString(Http.cookieLifetime()))
        val httpListCertificates get() = Reference<TfList>(referenceString(Http.certificates()))
        val healthChecks get() = Reference<TfList>(referenceString(HealthCheck.reference()))
        val healthChecksProtocols get() = Reference<TfList>(referenceString(HealthCheck.protocol()))
        val healthChecksPorts get() = Reference<TfList>(referenceString(HealthCheck.port()))
        val healthChecksIntervals get() = Reference<TfList>(referenceString(HealthCheck.interval()))
        val healthChecksTimeouts get() = Reference<TfList>(referenceString(HealthCheck.timeout()))
        val healthChecksRetriesList get() = Reference<TfList>(referenceString(HealthCheck.retries()))
        val healthChecksHttpLists get() = Reference<TfList>(referenceString(HealthCheck.httpList()))
        val healthChecksHttpListsDomains get() = Reference<TfList>(referenceString(HealthCheck.httpListDomains()))
        val healthChecksHttpListsPaths get() = Reference<TfList>(referenceString(HealthCheck.httpListPaths()))
        val healthChecksHttpListsResponses get() = Reference<TfList>(referenceString(HealthCheck.httpListResponses()))
        val healthChecksHttpListsTlsLists get() = Reference<TfList>(referenceString(HealthCheck.httpListTlsList()))
        val healthChecksHttpListsStatusCodesLists get() = Reference<TfList>(referenceString(HealthCheck.httpListStatusCodesList()))

        class Builder: GBuilder<Builder>() {
            private val loadBalancerIdBuilder: Argument.Builder = Argument.Builder().name(LOAD_BALANCER_ID)
            private val protocolBuilder: Argument.Builder = Argument.Builder().name(PROTOCOL)
            private val listenPortBuilder: Argument.Builder = Argument.Builder().name(LISTEN_PORT)
            private val destinationPortBuilder: Argument.Builder = Argument.Builder().name(DESTINATION_PORT)
            private val proxyprotocolBuilder: Argument.Builder = Argument.Builder().name(PROXYPROTOCL)
            private var httpList: List<Http> = mutableListOf()
            private var healthCheckList: List<HealthCheck> = mutableListOf()
            private lateinit var _protocol: Protocol
            init { resourceType(HCLOUD_LOAD_BALANCER_SERVICE) }

            fun loadBalancerId(loadBalancerId: Int) = apply { loadBalancerIdBuilder.value(loadBalancerId.toDouble()) }
            fun loadBalancerId(ref: Reference<TfNumber>) = apply { loadBalancerIdBuilder.raw(ref.toString()) }
            fun protocol(protocol: Protocol) = apply { _protocol = protocol; protocolBuilder.value(protocol.toString()) }
            fun listenPort(listenPort: Int) = apply { listenPortBuilder.value(listenPort.toDouble()) }
            fun listenPort(ref: Reference<TfNumber>) = apply { listenPortBuilder.raw(ref.toString()) }
            fun destinationPort(destinationPort: Int) = apply { destinationPortBuilder.value(destinationPort.toDouble()) }
            fun destinationPort(ref: Reference<TfNumber>) = apply { destinationPortBuilder.raw(ref.toString()) }
            fun proxyprotocol(proxyprotocol: Boolean) = apply { addElement(proxyprotocolBuilder.value(proxyprotocol).build()) }
            fun proxyprotocol(ref: Reference<TfBool>) = apply { addElement(proxyprotocolBuilder.raw(ref.toString()).build()) }
            fun http(http: Http) = apply { httpList = httpList + http }
            fun healthCheck(healthCheck: HealthCheck) = apply { healthCheckList = healthCheckList + healthCheck }
            override fun build(): Resource {
                addElement(loadBalancerIdBuilder.build())
                addElement(protocolBuilder.build())
                if (_protocol == Protocol.TCP) {
                    addElement(listenPortBuilder.build())
                    addElement(destinationPortBuilder.build())
                }
                if (_protocol == Protocol.HTTP || _protocol == Protocol.HTTPS) {
                    httpList.forEach {
                        addElement(it.block)
                    }
                    healthCheckList.forEach {
                        addElement(it.block)
                    }
                }
                return Resource(buildBlock(), buildSelf())
            }
        }

        fun http(index: Int) = Reference<Raw>(referenceString(Http.reference(index)))
        fun httpCookieName(index: Int) = Reference<TfString>(referenceString(Http.cookieName(index)))
        fun httpCookieLifetime(index: Int) = Reference<TfNumber>(referenceString(Http.cookieLifetime(index)))
        fun httpCertificate(index: Int) = Reference<TfList>(referenceString(Http.certificates(index)))
        fun healthCheck(index: Int) = Reference<Raw>(referenceString(HealthCheck.reference(index)))
        fun healthCheckProtocol(index: Int) = Reference<TfString>(referenceString(HealthCheck.protocol(index)))
        fun healthCheckPort(index: Int) = Reference<TfNumber>(referenceString(HealthCheck.port(index)))
        fun healthCheckInterval(index: Int) = Reference<TfNumber>(referenceString(HealthCheck.interval(index)))
        fun healthCheckTimeout(index: Int) = Reference<TfNumber>(referenceString(HealthCheck.timeout(index)))
        fun healthCheckRetries(index: Int) = Reference<TfNumber>(referenceString(HealthCheck.retries(index)))
        fun healthCheckHttpList(index: Int) = Reference<TfList>(referenceString(HealthCheck.httpList(index)))
        fun healthCheckHttpListDomains(index: Int) = Reference<TfList>(referenceString(HealthCheck.httpListDomains(index)))
        fun healthCheckHttpListPaths(index: Int) = Reference<TfList>(referenceString(HealthCheck.httpListPaths(index)))
        fun healthCheckHttpListResponses(index: Int) = Reference<TfList>(referenceString(HealthCheck.httpListResponses(index)))
        fun healthCheckHttpListTlsList(index: Int) = Reference<TfList>(referenceString(HealthCheck.httpListTlsList(index)))
        fun healthCheckHttpListStatusCodesList(index: Int) = Reference<TfList>(referenceString(HealthCheck.httpListStatusCodesList(index)))
        fun healthChecksHttp(httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.http(null, httpIndex)))
        fun healthChecksHttpDomain(httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListDomain(null, httpIndex)))
        fun healthChecksHttpPath(httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListPath(null, httpIndex)))
        fun healthChecksHttpResponse(httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListResponse(null, httpIndex)))
        fun healthChecksHttpTls(httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListTls(null, httpIndex)))
        fun healthChecksHttpStatusCodes(httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListStatusCodes(null, httpIndex)))
        fun healthCheckHttp(index: Int, httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.http(index, httpIndex)))
        fun healthCheckHttpDomain(index: Int, httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListDomain(index, httpIndex)))
        fun healthCheckHttpPath(index: Int, httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListPath(index, httpIndex)))
        fun healthCheckHttpResponse(index: Int, httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListResponse(index, httpIndex)))
        fun healthCheckHttpTls(index: Int, httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListTls(index, httpIndex)))
        fun healthCheckHttpStatusCodes(index: Int, httpIndex: Int) = Reference<TfList>(referenceString(HealthCheck.httpListStatusCodes(index, httpIndex)))
    }
}
