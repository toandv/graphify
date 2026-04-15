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
// When httpClient.get(path) is called, "get" resolves to ProxyClient.get (the caller) —
// a self-name collision. The fix records receiver="httpClient", callee_method="get" so
// cross-file resolution can find HttpClient.get instead of just HttpClient.
class ProxyClient(private val httpClient: HttpClient) {
    fun get(path: String): String = httpClient.get(path)
}
