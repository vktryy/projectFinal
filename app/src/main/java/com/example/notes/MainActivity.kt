import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.notes.data.NotesDatabase
import com.example.notes.theme.NotesTheme
import com.example.notes.ui.notes.NotesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotesDatabase.getInstance(applicationContext)
        setContent {
            NotesTheme {
                NotesScreen(
                    onAddNote = {
                        println("Добавить заметку")
                    },
                    onNoteClick = { note ->
                        println("Нажата заметка: ${note.title}")
                    }
                )
            }
        }
    }
}
