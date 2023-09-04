package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        String seriesName = webSeriesEntryDto.getSeriesName();

        if(webSeriesRepository.findBySeriesName(seriesName) != null){
            throw new Exception("Series is already present");
        }

        WebSeries webSeries = new WebSeries(webSeriesEntryDto.getSeriesName(), webSeriesEntryDto.getAgeLimit(),
                webSeriesEntryDto.getRating(), webSeriesEntryDto.getSubscriptionType());
        /*webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(web);
        */

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        productionHouse.getWebSeriesList().add(webSeries);

        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();

        double rating = 0;

        for(WebSeries webSeries1 : webSeriesList){
            rating += webSeries1.getRating();
        }

        double avgRating = rating/webSeriesList.size();
        productionHouse.setRatings(avgRating);

        webSeries.setProductionHouse(productionHouse);
        WebSeries saveWeb = webSeriesRepository.save(webSeries);
        productionHouseRepository.save(productionHouse);

        return saveWeb.getId();
    }

}
