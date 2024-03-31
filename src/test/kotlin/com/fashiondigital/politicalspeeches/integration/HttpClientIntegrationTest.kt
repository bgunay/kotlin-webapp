package com.fashiondigital.politicalspeeches.integration

import com.fashiondigital.politicalspeeches.exception.EvaluationServiceException
import com.fashiondigital.politicalspeeches.model.ErrorCode
import com.fashiondigital.politicalspeeches.service.impl.CsvParserService
import com.fashiondigital.politicalspeeches.service.impl.EvaluationService
import com.fashiondigital.politicalspeeches.util.HttpClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpClientIntegrationTest {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var csvParserService: CsvParserService


    @Autowired
    private val httpClient = HttpClient()
    @Autowired
    private val evaluationService = EvaluationService()


    @Test
    fun `test successful CSV download and processing`() = runBlocking {
        val urls = listOf(
            "http://localhost:81/valid-speeches-1.csv",
            "http://localhost:81/valid-speeches-2.csv"
        )

        val allSpeeches = urls.flatMap { url ->
            val csvContent = httpClient.getHttpCSVResponse(url).body
            val parseCSV = csvParserService.parseCSV(csvContent)
            parseCSV
        }

        val statistics = evaluationService.analyzeSpeeches(allSpeeches)

        assertEquals(null, statistics.mostSpeeches)
        assertEquals("Caesare Collins", statistics.mostSecurity)
        assertEquals("Alexander Abel", statistics.leastWordy)
    }

    @Test
    fun `test CSV download failure`() = runBlocking {
        val invalidUrl = "http://localhost:81/invalid-url"

        val exception = assertThrows<EvaluationServiceException> {
            httpClient.getHttpCSVResponse(invalidUrl)
        }

        assertTrue(exception.message?.contains(ErrorCode.URL_READER_ERROR.value) == true)
        assertTrue(exception.message?.contains("Failed to read url") == true)
    }
}