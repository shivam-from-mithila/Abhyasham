package in.practix.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("c/5132-eb91-4433-af02")
    Call<List<Subject>> getSubjects();

    @GET("c/7194-e52a-4a0a-ae22")
    Call<List<Topic>> getTopics();

    @GET("c/0538-dda2-46d9-992b")
    Call<List<Question>> getQuestions();
}