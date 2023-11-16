package com.unilim.iut.truckers.test.unit

import android.content.Context
import com.unilim.iut.truckers.commande.AjoutNumeroListeBlancheCommande
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File

class AjoutNumeroListeBlancheCommandeTest {

    private lateinit var context: Context
    private lateinit var tempFile: File

    @Before
    fun setUp() {
        context = Mockito.mock(Context::class.java)

        // Créer un fichier temporaire et écrire les données initiales
        tempFile = File.createTempFile("testListeBlanche", ".json")
        tempFile.writeText("{\"liste_blanche\": [\"numeroListeBlancheExistant\"]}")

        // Configurer le mock de Context pour retourner le chemin du fichier temporaire
        Mockito.`when`(context.filesDir).thenReturn(tempFile.parentFile)
    }

    @Test
    fun devraitRetournerFalseSiLeNumeroListeBlancheExisteDeja() {
        val commande = AjoutNumeroListeBlancheCommande(context, "numeroListeBlancheExistant")
        val resultat = commande.executer()
        assert(!resultat)

        // Nettoyer le fichier temporaire après le test
        tempFile.delete()
    }

    @Test
    fun devraitRetournerTrueSiLeNumeroListeBlancheNExistePas() {
        val commande = AjoutNumeroListeBlancheCommande(context, "numeroListeBlancheInexistant")
        val resultat = commande.executer()
        assert(resultat)

        // Nettoyer le fichier temporaire après le test
        tempFile.delete()
    }
}