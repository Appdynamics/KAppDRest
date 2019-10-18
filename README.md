# KAppDRest
KAppDRest provides wrapper functions on top of AppDynamics APIs. 
### Example
```kotlin
val kAppDRest = KAppDRest(
        "foo", // URL
        443,   // PORT
        true,  // SSL
        "foo", // USERNAME
        "foo", // PASSWORD
        "foo"  // ACCOUNT
    )

    val app: Application? = kAppDRest.getApplications().singleOrNull { it.name == "foo" }
    if (app != null) {
        val bts = kAppDRest.getBusinessTransactions(app.id)
        val tiers = kAppDRest.getTiers(app.id)
        val backends = kAppDRest.getBackends(app.id)
        val nodes = kAppDRest.getNodesByApplicationId(app.id)
        val node = kAppDRest.getNodeByNodeId(app.id, nodes[0].id)
        val nodes2 = kAppDRest.getNodesByTierId(app.id, nodes[0].tierId)
        val tier = kAppDRest.getTierByTierId(app.id, tiers[0].id)

        println("${app.name} has ${bts.size} business transactions.")
        println("${app.name} has ${tiers.size} tiers.")
        println("${app.name} has ${backends.size} backends.")
        println("${app.name} has ${nodes.size} nodes.")
        println("${app.name} has node ${node?.name} with ip addresses ${node?.ipAddresses}.")
        println("${app.name} has tier ${nodes[0].tierName} with ${nodes2.size} nodes.")
        println("${app.name} has tier ${tier?.id}.")

        val timeRange = TimeRange.beforeTime(123, 456)
        kAppDRest.getHealthRuleViolationsForApplication(app.id, timeRange)
    }
```
## Author
For support please email: bradley.hjelmar@appdynamics.com