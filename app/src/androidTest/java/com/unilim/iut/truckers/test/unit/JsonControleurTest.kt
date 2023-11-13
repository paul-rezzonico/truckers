import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.unilim.iut.truckers.controleur.JsonControleur
import com.unilim.iut.truckers.modele.Message
import com.unilim.iut.truckers.modele.NumeroTelephone
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import java.io.ByteArrayInputStream
import java.io.File

@RunWith(JUnit4::class)
class JsonControleurTest {

    private lateinit var context: Context
    private lateinit var jsonControleur: JsonControleur
    private val cheminFichier = "test.json"
    private val champs = "messages"
    private val message = Message(
        NumeroTelephone("0606060606"),
        "Test",
        "Test"
    )

    @Before
    fun setUp() {
        context = mock(Context::class.java)

        // Prepare a mocked files directory path
        val filesDir = mock(File::class.java)
        `when`(context.filesDir).thenReturn(filesDir)

        // Prepare the file path string
        val filePath = filesDir.absolutePath + File.separator + cheminFichier

        // Mock the file to simulate existence
        val file = mock(File::class.java)
        `when`(file.exists()).thenReturn(true)
        `when`(filesDir.absolutePath).thenReturn(filePath)

        // Assuming you have a method to get the file from the context, you need to mock that method
        `when`(context.getFileStreamPath(cheminFichier)).thenReturn(file)

        jsonControleur = JsonControleur()
    }

//    @Test
//    fun supprimer_removesMessageFromFile() {
//        // Create an ObjectMapper instance for JSON operations
//        val mapper = ObjectMapper().registerModule(KotlinModule())
//
//        // Prepare the Message object with a specific id for testing
//        val testMessage = Message(NumeroTelephone("0606060606"), "Test message", "2023-04-05", 1)
//
//        // Serialize the Message object to JSON
//        val messageJson = mapper.writeValueAsString(testMessage)
//
//        // Prepare a JSON array with the message to be removed
//        val initialJsonArray = JSONArray().apply { put(JSONObject(messageJson)) }
//
//        // Simulate the initial state of the file with the message
//        val jsonInputStream = ByteArrayInputStream(initialJsonArray.toString().toByteArray())
//        `when`(context.openFileInput(cheminFichier)).thenReturn(jsonInputStream)
//
//        // Perform the deletion operation
//        jsonControleur.supprimer(context, cheminFichier, champs, testMessage)
//
//        // Simulate the state of the file after the deletion operation
//        // Assuming 'supprimer' method modifies the file content
//        // You may need to adjust this to reflect the actual changes made by the method
//        val modifiedJsonArray = JSONArray()  // Empty array after deletion
//        val modifiedInputStream = ByteArrayInputStream(modifiedJsonArray.toString().toByteArray())
//        `when`(context.openFileInput(cheminFichier)).thenReturn(modifiedInputStream)
//
//        // Read the file content after deletion
//        val content = modifiedInputStream.bufferedReader().use { it.readText() }
//
//        // Deserialize the JSON back into an array of Messages
//        val resultingMessages: List<Message> = mapper.readValue(content)
//
//        // Assert that the list does not contain the deleted message
//        assertFalse("The message should have been removed", resultingMessages.contains(testMessage))
//    }
}
