// com/unilim/iut/truckers/test/unit/JsonControleurTest.kt
package com.unilim.iut.truckers.test.unit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.unilim.iut.truckers.controleur.JsonControleur
import com.unilim.iut.truckers.modele.JsonData
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class JsonControleurTest {

    private val jsonControleur = JsonControleur()
    private var context = ApplicationProvider.getApplicationContext<Context>()

    @After
    fun tearDown() {
        val file = File(context.filesDir, "test.json")
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun devraitCreerUnFichierJson() {
        jsonControleur.creationFichierJSON(JsonData(context, "test.json", "testField", null, null))

        val file = File(context.filesDir, "test.json")
        assertTrue(file.exists())
    }

    @Test
    fun devraitEcrireDansUnFichierJson() {
        val champs = "testField"
        val initialJsonContent = "{\"$champs\":[]}"
        createFileInFilesDir(context, "test.json", initialJsonContent)

        val result = jsonControleur.sauvegarderDonneesDansJSON(JsonData(context, "test.json", champs, "newTestValue", null))
        val file = File(context.filesDir, "test.json")

        assertTrue(result)
        assertTrue(file.exists())
    }

    @Test
    fun neDevraitPasEcrireDansUnFichierJsonSiIlNExistePasMaisLeCreer() {
        val champs = "testField"

        val result = jsonControleur.sauvegarderDonneesDansJSON(JsonData(context, "test.json", champs, "newTestValue", null))
        val file = File(context.filesDir, "test.json")

        assertTrue(!result)
        assertTrue(file.exists())
    }

    @Test
    fun devraitChargerUnFichierJson() {
        val champs = "testField"
        val initialJsonContent = "{\"$champs\":[\"testValue\"]}"
        createFileInFilesDir(context, "test.json", initialJsonContent)

        val result = jsonControleur.chargerDonneesDuJSON(JsonData(context, "test.json", champs, null, null))

        assertTrue(result.getString(champs) == "[\"testValue\"]")
    }

    @Test
    fun devraitSupprimerUnFichierJson() {
        val champs = "testField"
        val initialJsonContent = "{\"$champs\":[\"testValue\"]}"
        createFileInFilesDir(context, "test.json", initialJsonContent)

        val result = jsonControleur.suppressionFichierJSON(JsonData(context, "test.json", champs, null, null))
        val file = File(context.filesDir, "test.json")

        assertTrue(result)
        assertFalse(file.exists())
    }

    @Test
    fun neDevraitPasSupprimerUnFichierJsonSiIlNExistePas() {
        val result = jsonControleur.suppressionFichierJSON(JsonData(context, "test.json", null, null, null))
        val file = File(context.filesDir, "test.json")

        assertTrue(!result)
        assertFalse(file.exists())
    }

    @Test
    fun devraitSupprimerUnChampDansUnFichierJson() {
        val champs = "testField"
        val initialJsonContent = "{\"$champs\":[\"testValue\"]}"
        createFileInFilesDir(context, "test.json", initialJsonContent)

        val result = jsonControleur.supprimerDonneesDansJSON(JsonData(context, "test.json", champs, "testValue", null))
        val file = File(context.filesDir, "test.json")

        assertTrue(result)
        assertTrue(file.exists())
    }

    private fun createFileInFilesDir(context: Context, fileName: String, fileContent: String) {
        val file = File(context.filesDir, fileName)

        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(fileContent.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
