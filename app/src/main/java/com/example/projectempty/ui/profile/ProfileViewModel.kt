import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel : ViewModel() {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signOut() {
        mAuth.signOut()
    }
}