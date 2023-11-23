import android.util.Log
import com.example.ecoplay_front.apiService.ChallengeApi
import com.example.ecoplay_front.model.Comment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CommunityRepository(private val challengeApi: ChallengeApi) {

    fun fetchComments(challengeId: String, onSuccess: (List<Comment>?) -> Unit, onError: (String) -> Unit) {
        challengeApi.getComments(challengeId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                onError("Failure: ${t.message}")
            }
        })
    }

}
