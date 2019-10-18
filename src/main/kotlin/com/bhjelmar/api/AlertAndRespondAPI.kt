package com.bhjelmar.api

import com.bhjelmar.ApiRoute
import com.bhjelmar.KAppDRest

/**
 * This set of functions fully encapsulates the functionality of AppDynamics Alert and Respond API.
 * @see <a href="https://docs.appdynamics.com/display/PRO45/Alert+and+Respond+API">Alert and Respond API</a>
 *
 * @author Brad Hjelmar
 */

// https://docs.appdynamics.com/display/PRO45/Alert+and+Respond+API#AlertandRespondAPI-RetrieveAllHealthRuleViolationsinaBusinessApplication
fun KAppDRest.getHealthRuleViolationsForApplication(applicationId: Long, timeRangeType: TimeRange): List<Application> =
    invoke(
        KAppDRest.HttpMethod.GET,
        AlertAndRespondAPI.HEALTH_RULE_VIOLATIONS_FOR_APPLICATION,
        listOf(applicationId),
        timeRangeType.parameterize()
    )

private enum class AlertAndRespondAPI(
    override val route: String,
    override val routeParameters: List<String> = listOf("application_id", "entity_id"),
    override val parameters: List<Pair<String, Any>> = emptyList()
) : ApiRoute {
    HEALTH_RULE_VIOLATIONS_FOR_APPLICATION("/controller/rest/applications/application_id/problems/healthrule-violations"),
}

/**
 * TimeRange class can only be instantiated through factory methods.
 * Used for multiple API endpoints.
 */
class TimeRange private constructor(
    private val type: Type,
    private val parameters: List<Long>
) {
    companion object {
        fun beforeNow(durationInMins: Long): TimeRange =
            TimeRange(Type.BEFORE_NOW, listOf(durationInMins))

        fun beforeTime(durationInMins: Long, endTime: Long): TimeRange =
            TimeRange(Type.BEFORE_TIME, listOf(durationInMins, endTime))

        fun afterTime(durationInMins: Long, startTime: Long): TimeRange =
            TimeRange(Type.AFTER_TIME, listOf(durationInMins, startTime))

        fun betweenTimes(durationInMins: Long, endTime: Long): TimeRange =
            TimeRange(Type.BEFORE_NOW, listOf(durationInMins, endTime))
    }

    private enum class Type(
        val type: String,
        val parameters: List<String>
    ) {
        BEFORE_NOW("BEFORE_NOW", listOf("duration-in-mins")),
        BEFORE_TIME("BEFORE_TIME", listOf("duration-in-mins", "end-time")),
        AFTER_TIME("AFTER_TIME", listOf("duration-in-mins", "start-time")),
        BETWEEN_TIMES("BETWEEN_TIMES", listOf("start-time", "end-time"))
    }

    /**
     * Called by KAppDRest extension functions to convert data fields to HTTP parameter list.
     */
    fun parameterize(): List<Pair<String, String>> {
        val mappedParameters = type.parameters.zip(parameters.map { it.toString() })
        return listOf(listOf(Pair("time-range-type", type.type)), mappedParameters).flatten()
    }
}