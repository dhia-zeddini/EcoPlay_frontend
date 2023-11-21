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

    fun postComment(challengeId: String, userId: String, title: String, description: String, imageFile: File?, onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {
        // Convert title, description, and userId to RequestBody
        val titleBody = RequestBody.create("text/plain".toMediaTypeOrNull(), title)
        val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val userIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)

        // Create MultipartBody.Part using the File
        val imagePart: MultipartBody.Part? = imageFile?.let { file ->
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        challengeApi.postComment(challengeId, userIdBody, titleBody, descriptionBody, imagePart).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                onSuccess(response.isSuccessful)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onError("Failure: ${t.message}")
            }
        })
    }
}
