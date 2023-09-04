package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription = new Subscription();

        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());

        if(subscriptionEntryDto.getSubscriptionType().toString().equals("BASIC")){
            subscription.setTotalAmountPaid(500+200*subscriptionEntryDto.getNoOfScreensRequired());
        }
        else if(subscriptionEntryDto.getSubscriptionType().toString().equals("PRO")){
            subscription.setTotalAmountPaid(800+250*subscriptionEntryDto.getNoOfScreensRequired());
        }
        else if(subscriptionEntryDto.getSubscriptionType().toString().equals("ELITE")){
            subscription.setTotalAmountPaid(1000+350*subscriptionEntryDto.getNoOfScreensRequired());
        }

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        subscription.setUser(user);
        user.setSubscription(subscription);
        userRepository.save(user);
        return subscription.getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        User user = userRepository.findById(userId).get();
        SubscriptionType present = user.getSubscription().getSubscriptionType();
        if(present.toString().equals("ELITE"))
            throw new Exception("Already the best Subscription");
        Subscription presSub = user.getSubscription();
        Integer presPrice = presSub.getTotalAmountPaid();
        Integer presNoOfScreens = presSub.getNoOfScreensSubscribed();

        Integer diff = 0;
        if(presSub.getSubscriptionType().toString().equals("BASIC")){
            Integer upgradePrice = 800+250*presNoOfScreens;
            diff = upgradePrice - presPrice;

            presSub.setSubscriptionType(SubscriptionType.PRO);
            presSub.setTotalAmountPaid(upgradePrice);
        }
        else if(presSub.getSubscriptionType().toString().equals("PRO")){
            Integer upgradePrice = 1000+350*presNoOfScreens;
            diff = upgradePrice - presPrice;

            presSub.setSubscriptionType(SubscriptionType.ELITE);
            presSub.setTotalAmountPaid(upgradePrice);
        }

        presSub.setUser(user);
        user.setSubscription(presSub);

        userRepository.save(user);
        return diff;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptionList = subscriptionRepository.findAll();

        Integer totalRevenue = 0;

        for(Subscription subscription : subscriptionList){
            totalRevenue += subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
