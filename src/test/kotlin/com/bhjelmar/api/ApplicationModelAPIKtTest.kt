package com.bhjelmar.api

import com.bhjelmar.KAppDRest
import com.bhjelmar.fromJson
import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkConstructor
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import javax.xml.ws.http.HTTPException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationModelAPIKtTest {

    @MockK
    lateinit var successClient: Client
    @MockK
    lateinit var unauthorizedClient: Client

    @MockK
    lateinit var mockApplication: Application
    @MockK
    lateinit var mockBusinessTransaction: BusinessTransaction
    @MockK
    lateinit var mockTier: Tier
    @MockK
    lateinit var mockBackend: Backend
    @MockK
    lateinit var mockNode: Node

    private val someJson = "{\"foo\":\"bar\"}"
    private val controller = KAppDRest("foo", 1, true, "bar", "baz", "qux")

    @BeforeAll
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { successClient.executeRequest(any()).statusCode } returns 200
        every { successClient.executeRequest(any()).responseMessage } returns "OK"
        every { successClient.executeRequest(any()).data } returns someJson.toByteArray()

        every { unauthorizedClient.executeRequest(any()).statusCode } returns 401
        every { unauthorizedClient.executeRequest(any()).responseMessage } returns "Unauthorized"
        every { unauthorizedClient.executeRequest(any()).data } throws HTTPException(401)

        mockkConstructor(Gson::class)
    }

    @Test
    fun `getApplications success`() {
        // given
        every { anyConstructed<Gson>().fromJson<List<Application>>(someJson) } returns listOf(mockApplication)
        FuelManager.instance.client = successClient

        // when
        val apps: List<Application> = controller.getApplications()

        // then
        assert(apps[0] == mockApplication)
    }

    @Test
    fun `getApplications authentication failure throws HTTPException`() {
        // given
        FuelManager.instance.client = unauthorizedClient

        // when

        // then
        assertThrows<HTTPException> { controller.getApplications() }
    }

    @Test
    fun `getBusinessTransactions success`() {
        // given
        every { anyConstructed<Gson>().fromJson<List<BusinessTransaction>>(someJson) } returns listOf(
            mockBusinessTransaction
        )
        FuelManager.instance.client = successClient

        // when
        val businessTransactions: List<BusinessTransaction> = controller.getBusinessTransactions(1)

        // then
        assert(businessTransactions[0] == mockBusinessTransaction)
    }

    @Test
    fun `getBusinessTransactions authentication failure throws HTTPException`() {
        // given
        FuelManager.instance.client = unauthorizedClient

        // when

        // then
        assertThrows<HTTPException> { controller.getBusinessTransactions(1) }
    }

    @Test
    fun `getTiers success`() {
        // given
        every { anyConstructed<Gson>().fromJson<List<Tier>>(someJson) } returns listOf(mockTier)
        FuelManager.instance.client = successClient

        // when
        val tiers: List<Tier> = controller.getTiers(1)

        // then
        assert(tiers[0] == mockTier)
    }

    @Test
    fun `getTiers authentication failure throws HTTPException`() {
        // given
        FuelManager.instance.client = unauthorizedClient

        // when

        // then
        assertThrows<HTTPException> { controller.getTiers(1) }
    }

    @Test
    fun `getTierByTierId success`() {
        // given
        every { anyConstructed<Gson>().fromJson<List<Tier>>(someJson) } returns listOf(mockTier)
        FuelManager.instance.client = successClient

        // when
        val tier: Tier? = controller.getTierByTierId(1, 1)

        // then
        assert(mockTier == tier)
    }

    @Test
    fun `getTierByTierId authentication failure throws HTTPException`() {
        // given
        FuelManager.instance.client = unauthorizedClient

        // when

        // then
        assertThrows<HTTPException> { controller.getTierByTierId(1, 1) }
    }

    @Test
    fun `getBackends success`() {
        // given
        every { anyConstructed<Gson>().fromJson<List<Backend>>(someJson) } returns listOf(mockBackend)
        FuelManager.instance.client = successClient

        // when
        val backends: List<Backend> = controller.getBackends(1)

        // then
        assert(backends[0] == mockBackend)
    }

    @Test
    fun `getBackends authentication failure throws HTTPException`() {
        // given
        FuelManager.instance.client = unauthorizedClient

        // when

        // then
        assertThrows<HTTPException> { controller.getBackends(1) }
    }

    @Test
    fun `getNodesByApplicationId success`() {
        // given
        every { anyConstructed<Gson>().fromJson<List<Node>>(someJson) } returns listOf(mockNode)
        FuelManager.instance.client = successClient

        // when
        val nodes: List<Node> = controller.getNodesByApplicationId(1)

        // then
        assert(nodes[0] == mockNode)
    }

    @Test
    fun `getNodesByApplicationId authentication failure throws HTTPException`() {
        // given
        FuelManager.instance.client = unauthorizedClient

        // when

        // then
        assertThrows<HTTPException> { controller.getNodesByApplicationId(1) }
    }

    @Test
    fun `getNodeByNodeId success`() {
        // given
        every { anyConstructed<Gson>().fromJson<List<Node>>(someJson) } returns listOf(mockNode)
        FuelManager.instance.client = successClient

        // when
        val node: Node? = controller.getNodeByNodeId(1, 1)

        // then
        assert(mockNode == node)
    }

    @Test
    fun `getNodeByNodeId authentication failure throws HTTPException`() {
        // given
        FuelManager.instance.client = unauthorizedClient

        // when

        // then
        assertThrows<HTTPException> { controller.getNodesByApplicationId(1) }
    }

    @Test
    fun `getNodesByTierId success`() {
        // given
        every { anyConstructed<Gson>().fromJson<List<Node>>(someJson) } returns listOf(mockNode)
        FuelManager.instance.client = successClient

        // when
        val nodes: List<Node> = controller.getNodesByTierId(1, 1)

        // then
        assert(nodes[0] == mockNode)
    }

    @Test
    fun `getNodesByTierId authentication failure throws HTTPException`() {
        // given
        FuelManager.instance.client = unauthorizedClient

        // when

        // then
        assertThrows<HTTPException> { controller.getNodesByTierId(1, 1) }
    }
}