package com.unilim.iut.truckers.test.unit

import android.content.Context
import com.unilim.iut.truckers.commande.AjoutMotCleCommande
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File

class AjoutMotCleCommandeTest {

    private lateinit var tempFile: File

    @Before
    fun setUp() {
        val mockContext = Mockito.mock(Context::class.java)

        val classLoader = javaClass.classLoader
        val testFile = classLoader?.getResource("MotsCles.json")?.file?.let { File(it) }

        if (testFile != null) {
            Mockito.`when`<Any>(mockContext.filesDir).thenReturn(testFile.parentFile)
        }
    }

    @Test
    fun devraitRetournerTrueSiLeMotCleAEteAjoute() {
        val mockContext = Mockito.mock(Context::class.java)
        val ajoutMotCleCommande = AjoutMotCleCommande(mockContext, "test")

        val classLoader = javaClass.classLoader
        val testFile = classLoader?.getResource("MotsCles.json")?.file?.let { File(it) }

        if (testFile != null) {
            Mockito.`when`<Any>(mockContext.filesDir).thenReturn(testFile.parentFile)
        }

        val resultat = ajoutMotCleCommande.executer()

        assert(resultat)
    }

    private fun doitEcrireLesMotsClee() {
        val keywords: JSONArray = JSONArray();
        keywords.put("RENDEZ-VOUS");
        keywords.put("RDV");
        keywords.put("LIVRAISON");
        keywords.put("LIV");
        keywords.put("URGENT");
        keywords.put("URG");
        keywords.put("INFORMATION");
        keywords.put("INF")

        // Create a JSON Object to hold the array
        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("mots_cles", keywords);

        val testFile = javaClass.classLoader?.getResource("MotsCles.json")?.file?.let { File(it) }

        if (testFile != null) {
            val fluxSortie = testFile.outputStream()
            fluxSortie.write(jsonObject.toString(4).toByteArray())
            fluxSortie.close()
        }
    }

    private
}
