package com.unilim.iut.truckers.test.unit

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.unilim.iut.truckers.commande.AjoutMotCleCommande
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File

class AjoutMotCleCommandeTest {

    private var context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        context = Mockito.mock(Context::class.java)

        val classLoader = javaClass.classLoader
        val testFile = classLoader?.getResource("MotsCles.json")?.file?.let { File(it) }
        Mockito.`when`<Any>(File(context.filesDir, "MotsCles.json")).thenReturn(testFile)
    }

    @Test
    fun devraitRetournerTrueSiLeMotCleAEteAjoute() {
        val mockContext = Mockito.mock(Context::class.java)
        val ajoutMotCleCommande = AjoutMotCleCommande(mockContext, "test")

        val file = File(mockContext.filesDir, "MotsCles.json")

        Log.e("test", "{${context.filesDir}}")
        Log.e("test", "{${file.exists()}}")
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
}
