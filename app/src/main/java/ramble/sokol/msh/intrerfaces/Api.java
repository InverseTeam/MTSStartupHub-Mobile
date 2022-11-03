package ramble.sokol.msh.intrerfaces;

import ramble.sokol.msh.models.PostEvents;
import ramble.sokol.msh.models.PostRegistration;
import ramble.sokol.msh.models.PostToken;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @POST("/api/auth/users/")
    @FormUrlEncoded
    Call<PostRegistration> savePost(
            @Field("email") String email,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("password") String password
    );

    @POST("/auth/token/login/")
    @FormUrlEncoded
    Call<PostToken> getToken(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("/api/events/")
    @FormUrlEncoded
    Call<PostEvents> postEvents(
            @Field("name") String name,
            @Field("platform") String platform,
            @Field("description") String description,
            @Field("goals") String goals,
            @Field("equipment") String equipment,
            @Field("theme") String theme,
            @Field("date") String date,
            @Field("time") String time,
            @Field("add_information") String addInformation
    );
}
