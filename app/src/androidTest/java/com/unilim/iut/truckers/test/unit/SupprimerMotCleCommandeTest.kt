package com.unilim.iut.truckers.test.unit

import android.content.Context
import com.unilim.iut.truckers.commande.SupprimerMotCleCommande
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File

class SupprimerMotCleCommandeTest {

    private lateinit var context: Context
    private lateinit var tempFile: File

    @Before
    fun setUp() {
        context = Mockito.mock(Context::class.java)

        // Créer un fichier temporaire et écrire les données initiales
        tempFile = File.createTempFile("testMotsCles", ".json")
        tempFile.writeText("{\"mots_cles\": [\"motCleExistant\"]}")

        // Configurer le mock de Context pour retourner le chemin du fichier temporaire
        Mockito.`when`(context.filesDir).thenReturn(tempFile.parentFile)
    }

    @Test
    fun devraitRetournerTrueSiLeMotCleExiste() {
        val commande = SupprimerMotCleCommande(context, "motCleExistant")
        val resultat = commande.executer()
        assert(resultat)

        // Nettoyer le fichier temporaire après le test
        tempFile.delete()
    }

    @Test
    fun devraitRetournerTrueSiLeMotCleNExistePas() {
        val commande = SupprimerMotCleCommande(context, "motCleInexistant")
        val resultat = commande.executer()
        assert(resultat)

        // Nettoyer le fichier temporaire après le test
        tempFile.delete()
    }
}