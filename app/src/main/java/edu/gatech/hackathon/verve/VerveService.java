package edu.gatech.hackathon.verve;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by adhir on 1/27/2018.
 */

public interface VerveService {

    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<Token> login(@Body String info);

    @Multipart
    @POST("/upload")
    Call<List<Item>> bill(@Part MultipartBody.Part bill);

    @GET("/donate")
    Call<List<Charity>> organizations();
}
