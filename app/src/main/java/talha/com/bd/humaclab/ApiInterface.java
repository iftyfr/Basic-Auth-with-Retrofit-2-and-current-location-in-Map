package talha.com.bd.humaclab;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> userLogin(
            @Header("Authorization") String token,
            @Field("email") String username,
            @Field("password") String password
    );
}
