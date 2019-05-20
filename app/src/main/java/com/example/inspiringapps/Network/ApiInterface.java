package com.example.inspiringapps.Network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

public interface ApiInterface {

    String API_KEY = "30E02AAA-B947-4D4B-8FB6-9C57C43872A9";

    //Make the api call return an observable instead of a call back
    @GET("/Files/IAChallenge/" + API_KEY + "/Apache.log")
    Observable<ResponseBody> downloadFile();
}
