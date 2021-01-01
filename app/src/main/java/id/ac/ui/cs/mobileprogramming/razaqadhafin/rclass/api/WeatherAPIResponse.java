package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.api;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "data", strict = false)
public class WeatherAPIResponse {

    @ElementList(name = "area", inline = true)
    @Path("forecast")
    private List<Area> weatherList;

    public List<Area> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Area> weatherList) {
        this.weatherList = weatherList;
    }
}
