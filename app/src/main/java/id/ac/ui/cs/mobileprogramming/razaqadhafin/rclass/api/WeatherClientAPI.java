package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WeatherClientAPI {

    @GET("datamkg/MEWS/DigitalForecast/DigitalForecast-JawaBarat.xml")
    Call<WeatherAPIResponse> getWeather();

}
