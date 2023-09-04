package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        User user = userRepository.findById(userId).get();
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();

        Integer count = 0;
        String userSubType = user.getSubscription().getSubscriptionType().toString();
        for(WebSeries webSeries : webSeriesList){
            String webSeriesSubType = webSeries.getSubscriptionType().toString();
            if(user.getAge() >= webSeries.getAgeLimit()){
                if(userSubType.equals("ELITE")) count++;
                else if(userSubType.equals(webSeriesSubType)) count++;
                else if(userSubType.equals("PRO") && webSeriesSubType.equals("BASIC")) count++;
                else continue;
            }
        }
        return count;
    }


}
