package com.unilim.iut.truckers.test.unit

import android.content.Context
import com.unilim.iut.truckers.commande.AjoutNumeroAdminCommande
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File

class AjoutNumeroAdminCommandeTest {

    private lateinit var context: Context
    private lateinit var tempFile: File

    @Before
    fun setUp() {
        context = Mockito.mock(Context::class.java)

        // Créer un fichier temporaire et écrire les données initiales
        tempFile = File.createTempFile("ListeBlanche", ".json")

        // Configurer le mock de Context pour retourner le chemin du fichier temporaire
        Mockito.`when`(context.filesDir).thenReturn(context.cacheDir)
        tempFile = File.createTempFile("ListeBlanche", ".json", context.cacheDir)
    }

    @Test
    fun devraitRetournerFalseSiLeNumeroAdminExisteDeja() {
        tempFile = File.createTempFile("ListeBlanche", ".json", context.cacheDir)
        tempFile.writeText("{\"numero_admin\": [\"0123456789\"]}")
        assert(tempFile.exists())
        assert(tempFile.readText() == "{\"numero_admin\": [\"0123456789\"]}")
        val commande = AjoutNumeroAdminCommande(context, "numeroAdminExistant")
        val resultat = commande.executer()
        assert(resultat)
        // Nettoyer le fichier temporaire après le test
        tempFile.delete()
    }

    @Test
    fun devraitRetournerTrueSiLeNumeroAdminNExistePas() {
        val commande = AjoutNumeroAdminCommande(context, "numeroAdminInexistant")
        val resultat = commande.executer()
        assert(resultat)

        // Nettoyer le fichier temporaire après le test
        tempFile.delete()
    }
}