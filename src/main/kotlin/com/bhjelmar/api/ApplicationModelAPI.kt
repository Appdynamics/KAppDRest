package com.bhjelmar.api

import com.bhjelmar.ApiRoute
import com.bhjelmar.KAppDRest

/**
 * This set of functions fully encapsulates the functionality of AppDynamics Application Model API.
 * https://docs.appdynamics.com/display/PRO45/Application+Model+API
 *
 * @author Brad Hjelmar
 */

// https://docs.appdynamics.com/display/PRO45/Application+Model+API#ApplicationModelAPI-RetrieveAllBusinessApplications
fun KAppDRest.getApplications(): List<Application> =
    invoke(KAppDRest.HttpMethod.GET, ApplicationModelAPI.APPLICATIONS)

// https://docs.appdynamics.com/display/PRO45/Application+Model+API#ApplicationModelAPI-RetrieveAllBusinessTransactionsinaBusinessApplication
fun KAppDRest.getBusinessTransactions(applicationId: Long): List<BusinessTransaction> =
    invoke(KAppDRest.HttpMethod.GET, ApplicationModelAPI.BUSINESS_TRANSACTIONS, listOf(applicationId))

// https://docs.appdynamics.com/display/PRO45/Application+Model+API#ApplicationModelAPI-RetrieveAllTiersinaBusinessApplication
fun KAppDRest.getTiers(applicationId: Long): List<Tier> =
    invoke(KAppDRest.HttpMethod.GET, ApplicationModelAPI.TIERS, listOf(applicationId))

// https://docs.appdynamics.com/display/PRO45/Application+Model+API#ApplicationModelAPI-RetrieveTierInformationbyTierName
fun KAppDRest.getTierByTierId(applicationId: Long, tierId: Long): Tier? =
    invoke<List<Tier>>(KAppDRest.HttpMethod.GET, ApplicationModelAPI.TIER_BY_TIER_ID, listOf(applicationId, tierId))[0]

// https://docs.appdynamics.com/display/PRO45/Application+Model+API#ApplicationModelAPI-RetrieveAllRegisteredBackendsinaBusinessApplicationWithTheirProperties
fun KAppDRest.getBackends(applicationId: Long): List<Backend> =
    invoke(KAppDRest.HttpMethod.GET, ApplicationModelAPI.BACKENDS, listOf(applicationId))

// https://docs.appdynamics.com/display/PRO45/Application+Model+API#ApplicationModelAPI-RetrieveNodeInformationforAllNodesinaBusinessApplication
fun KAppDRest.getNodesByApplicationId(applicationId: Long): List<Node> =
    invoke(KAppDRest.HttpMethod.GET, ApplicationModelAPI.NODES_BY_APPLICATION_ID, listOf(applicationId))

// https://docs.appdynamics.com/display/PRO45/Application+Model+API#ApplicationModelAPI-RetrieveNodeInformationbyNodeName
fun KAppDRest.getNodeByNodeId(applicationId: Long, nodeId: Long): Node? =
    invoke<List<Node>>(KAppDRest.HttpMethod.GET, ApplicationModelAPI.NODES_BY_NODE_ID, listOf(applicationId, nodeId))[0]

// https://docs.appdynamics.com/display/PRO45/Application+Model+API#ApplicationModelAPI-RetrieveNodeInformationforAllNodesinaTier
fun KAppDRest.getNodesByTierId(applicationId: Long, tierId: Long): List<Node> =
    invoke(KAppDRest.HttpMethod.GET, ApplicationModelAPI.NODES_BY_TIER_ID, listOf(applicationId, tierId))

data class Application(
    val name: String,
    val description: String,
    val id: Long,
    val accountGuid: String
)

data class BusinessTransaction(
    val id: Long,
    val name: String,
    val entryPointType: String,
    val internalName: String,
    val tierId: Long,
    val tierName: String,
    val background: Boolean
)

data class Tier(
    val id: Long,
    val name: String,
    val type: String,
    val agentType: String,
    val numberOfNodes: Int
)

data class Backend(
    val id: Long,
    val name: String,
    val exitPointType: String,
    val properties: List<Property>,
    val applicationComponentNodeId: Long,
    val tierId: Long
) {
    data class Property(
        val id: Long,
        val name: String,
        val value: String
    )
}

data class Node(
    val id: Long,
    val name: String,
    val type: String,
    val tierId: Long,
    val tierName: String,
    val machineId: Long,
    val machineName: String,
    val machineOSType: String,
    val machineAgentPresent: Boolean,
    val machineAgentVersion: String,
    val appAgentPresent: Boolean,
    val appAgentVersion: String,
    val ipAddresses: IpAddresses,
    val agentType: String
) {
    data class IpAddresses(
        val ipAddresses: List<String>
    )
}

private enum class ApplicationModelAPI(
    override val route: String,
    override val routeParameters: List<String> = listOf("application_id", "entity_id"),
    override val parameters: List<Pair<String, Any>> = emptyList()
) : ApiRoute {
    APPLICATIONS("/controller/rest/applications"),
    BUSINESS_TRANSACTIONS("/controller/rest/applications/application_id/business-transactions"),
    TIERS("/controller/rest/applications/application_id/tiers"),
    BACKENDS("/controller/rest/applications/application_id/backends"),
    NODES_BY_APPLICATION_ID("/controller/rest/applications/application_id/nodes"),
    NODES_BY_NODE_ID("/controller/rest/applications/application_id/nodes/entity_id"),
    NODES_BY_TIER_ID("/controller/rest/applications/application_id/tiers/entity_id/nodes"),
    TIER_BY_TIER_ID("/controller/rest/applications/application_id/tiers/entity_id")
}