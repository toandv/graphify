import kotlinx.coroutines.delay
import kotlin.math.max

data class Config(val baseUrl: String, val timeout: Int)

class HttpClient(private val config: Config) {
    fun get(path: String): String {
        return buildRequest("GET", path)
    }

    fun post(path: String, body: String): String {
        return buildRequest("POST", path)
    }

    private fun buildRequest(method: String, path: String): String {
        return "$method ${config.baseUrl}$path"
    }
}

fun createClient(baseUrl: String): HttpClient {
    val config = Config(baseUrl, 30)
    return HttpClient(config)
}

// Self-name collision fixture: get() here has the same name as HttpClient.get() above.
// When this calls delegate.get(path), the extractor must NOT silently discard the call
// because "get" resolves to the caller itself — it should fall back to "delegate".
class ProxyClient(private val delegate: HttpClient) {
    fun get(path: String): String = delegate.get(path)
}
