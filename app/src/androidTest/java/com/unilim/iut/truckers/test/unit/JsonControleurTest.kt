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
        operationFichierJson("testField", "[]") { jsonControleur.sauvegarderDonneesDansJSON(it) }

    }

    @Test
    fun neDevraitPasEcrireDansUnFichierJsonSiIlNExistePasMaisLeCreer() {
        operationFichierJson("testField", "[]") { jsonControleur.sauvegarderDonneesDansJSON(it) }
    }

    @Test
    fun devraitSupprimerUnFichierJson() {
        operationFichierJson("testField", "[\"testValue\"]") { jsonControleur.suppressionFichierJSON(it) }
    }

    @Test
    fun neDevraitPasSupprimerUnFichierJsonSiIlNExistePas() {
        operationFichierJson(null, null) { jsonControleur.suppressionFichierJSON(it) }
    }

    @Test
    fun devraitSupprimerUnChampDansUnFichierJson() {
        operationFichierJson("testField", "[\"testValue\"]") { jsonControleur.supprimerDonneesDansJSON(it) }
    }

    private fun operationFichierJson(
        champs: String?,
        valeur: String?,
        operation: (JsonData) -> Boolean
    ) {
        if (champs != null && valeur != null) {
            creeFichierDansDossier(context, "test.json", "{\"$champs\":$valeur}")
        }

        val resultat = operation(JsonData(context, "test.json", champs, null, null))
        val fichier = File(context.filesDir, "test.json")

        assertTrue(resultat)
        if (champs != null) {
            assertFalse(fichier.exists())
        } else {
            assertTrue(fichier.exists())
        }
    }

    private fun creeFichierDansDossier(contexte: Context, nomFichier: String, valeurFichier: String) {
        val fichier = File(contexte.filesDir, nomFichier)

        try {
            FileOutputStream(fichier).use { outputStream ->
                outputStream.write(valeurFichier.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
