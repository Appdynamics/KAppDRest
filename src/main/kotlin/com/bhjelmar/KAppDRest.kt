package com.bhjelmar

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import javax.xml.ws.http.HTTPException

class KAppDRest(
    url: String,
    port: Int,
    ssl: Boolean,
    val username: String,
    val password: String,
    val account: String
) {

    init {
        FuelManager.instance.baseParams = listOf("output" to "json")
    }

    val logger = KotlinLogging.logger {}
    val endpoint = "${if (ssl) "https://" else "http://"}$url:$port"

    val gson = Gson()

    internal inline fun <reified T> invoke(
        httpMethod: HttpMethod,
        api: ApiRoute,
        routeParams: List<Any> = emptyList(),
        params: List<Pair<String, Any>> = emptyList(),
        body: String = ""
    ): T {
        // replace api route placeholders with parameters
        var route = api.route
        routeParams.forEachIndexed { i, s -> route = route.replace(api.routeParameters[i], s.toString()) }

        val request: Request =
            when (httpMethod) {
                HttpMethod.GET -> Fuel.get((endpoint + route), params)
                HttpMethod.PUT -> Fuel.put((endpoint + route), params)
                HttpMethod.POST -> Fuel.post((endpoint + route), params)
                HttpMethod.DELETE -> Fuel.delete((endpoint + route), params)
            }
        val (_, response, result) = request.authentication()
            .basic("$username@$account", password)
            .body(body)
            .responseString()
        return when (result) {
            is Result.Failure -> {
                logger.error { "$endpoint+$route ${result.getException().message}" }
                throw HTTPException(response.statusCode)
            }
            is Result.Success -> {
                logger.info { "HTTP ${response.statusCode} $endpoint+$route" }
                gson.fromJson(result.value)
            }
        }
    }

    internal enum class HttpMethod {
        GET,
        PUT,
        POST,
        DELETE
    }

}

// Used for generic JSON deserialization
inline fun <reified T> typeToken() = object : TypeToken<T>() {}.type!!

inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, typeToken<T>())

interface ApiRoute {
    val route: String
    val routeParameters: List<String>
    val parameters: List<Pair<String, Any>>
}