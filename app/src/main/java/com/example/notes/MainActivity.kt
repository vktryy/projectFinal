import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.notes.data.NotesDatabase
import com.example.notes.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация Room
        NotesDatabase.getInstance(applicationContext)

        setContent {
            NotesTheme {
                NotesScreen()
            }
        }
    }
}

class NotesScreen {

}
