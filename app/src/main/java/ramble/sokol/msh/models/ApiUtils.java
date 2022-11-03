package ramble.sokol.msh.models;

import ramble.sokol.msh.intrerfaces.Api;

public class ApiUtils {

    private ApiUtils(){}

    public static final String BASE_URL = "http://10.0.2.2:8080/";

    public static Api getApi(){
        return RetrofitClient.getClient(BASE_URL).create(Api.class);
    }

}
