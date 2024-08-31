package com.example.weather_app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.weather_app.model.WeatherResponse;

@Controller
public class WeatherController {

    @Value("${api.key}")
    private String apiKey; 
@GetMapping("/abc")
public String getIndex(){
    return "index";
}

@GetMapping("/weather")
public String getWeather(@RequestParam("city") String city, Model model) {
    // Construct the URL with the city and API key
    String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

    // Log the constructed URL
    System.out.println("Request URL: " + url);

    // Create a RestTemplate instance
    RestTemplate restTemplate = new RestTemplate();
    
    try {
        // Make the API call and retrieve the weather response
        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

        // Process the response
        if (weatherResponse != null) {
            model.addAttribute("city", weatherResponse.getName());
            model.addAttribute("country", weatherResponse.getSys().getCountry());
            model.addAttribute("weatherDescription", weatherResponse.getWeather().get(0).getDescription());
            model.addAttribute("temperature", weatherResponse.getMain().getTemp());
            model.addAttribute("humidity", weatherResponse.getMain().getHumidity());
            model.addAttribute("windSpeed", weatherResponse.getWind().getSpeed());
            String weatherIcon = "wi wi-owm-" + weatherResponse.getWeather().get(0).getId();
            model.addAttribute("weatherIcon", weatherIcon);
        } else {
            model.addAttribute("error", "City not found.");
        }
    } catch (HttpClientErrorException e) {
        model.addAttribute("error", "Error fetching weather data: " + e.getStatusCode());
    }

    return "weather";
}


}
