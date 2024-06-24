package com.roomoccupancy.service;

import com.roomoccupancy.dto.OccupancyDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.roomoccupancy.model.GuestInterest;
import com.roomoccupancy.repository.GuestInterestRepository;

import java.math.BigDecimal;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
public class OccupancyServiceImpl implements OccupancyService {

    @Autowired
    private GuestInterestRepository guestInterestRepository;

    public OccupancyDetails getOccupancyForDate(int premiumRooms, int economyRooms, Date date){
        List<GuestInterest> guestInterests = guestInterestRepository.findByDate(date);
        return  calculateOccupancy(premiumRooms, economyRooms, guestInterests);
    }

    public OccupancyDetails getOccupancy(int premiumRooms, int economyRooms){
        Date latestDate = guestInterestRepository.findLatestDate();
        List<GuestInterest> guestInterests = guestInterestRepository.findByDate(latestDate);
        return  calculateOccupancy(premiumRooms, economyRooms, guestInterests);
    }

    protected OccupancyDetails calculateOccupancy(int premiumRooms, int economyRooms, List<GuestInterest> guestInterests){
        LinkedList<BigDecimal> economyCandidates = new LinkedList<BigDecimal>();
        LinkedList<BigDecimal> premiumCandidates = new LinkedList<BigDecimal>();

        initializeLists(economyCandidates, premiumCandidates, guestInterests);

        OccupancyDetails.OccupancyDetailsBuilder occupancyDetailsBuilder =
                calculatePremiumUsageAndIncome(premiumRooms, economyRooms, premiumCandidates, economyCandidates);

        return calculateEconomyUsageAndIncome(economyRooms, economyCandidates, occupancyDetailsBuilder);
    }

    private void initializeLists(List<BigDecimal> economyCandidates, LinkedList<BigDecimal> premiumCandidates,
                                 List<GuestInterest> guestInterests){
        for(GuestInterest interest : guestInterests) {
            if( checkPremiumCandidate(interest.getPrice()) ) {
                premiumCandidates.add(interest.getPrice());
            } else {
                economyCandidates.add(interest.getPrice());
            }
        }

        Collections.sort(economyCandidates);
        Collections.reverse(economyCandidates);
        Collections.sort(premiumCandidates);
        Collections.reverse(premiumCandidates);
    }

    private OccupancyDetails.OccupancyDetailsBuilder
    calculatePremiumUsageAndIncome(int premiumRooms, int economyRooms,
                                   LinkedList<BigDecimal> premiumCandidates,
                                   LinkedList<BigDecimal> economyCandidates){
        int premiumInterestCnt = premiumCandidates.size();
        int economyInterestCnt = economyCandidates.size();

        BigDecimal premiumIncome = calculateOccupacyForAvailable(premiumRooms, premiumCandidates);
        int takenPremium = premiumInterestCnt - premiumCandidates.size();
        int freePremium = checkIfLeftAnyPremiumRoom(premiumInterestCnt, premiumRooms, premiumCandidates.size());
        freePremium = calculateHowManyEconomyUpgradeToPremiumPossible(freePremium, economyRooms, economyInterestCnt);
        BigDecimal premiumIncomeFromEconomyCandidates = calculateOccupacyForAvailable(freePremium, economyCandidates);
        premiumIncome = premiumIncome.add(premiumIncomeFromEconomyCandidates);
        takenPremium += (economyInterestCnt - economyCandidates.size());

        return OccupancyDetails.builder()
                .premiumIncome(premiumIncome)
                .premiumUsage(takenPremium);
    }

    private OccupancyDetails calculateEconomyUsageAndIncome(int economyRooms,
                                                            LinkedList<BigDecimal> economyCandidates,
                                                            OccupancyDetails.OccupancyDetailsBuilder occupancyDetailsBuilder){
        int economyInterestCnt = economyCandidates.size();
        BigDecimal economyIncome = calculateOccupacyForAvailable(economyRooms, economyCandidates);
        int takenEconomy = (economyInterestCnt - economyCandidates.size());

        return occupancyDetailsBuilder
                .economyUsage(takenEconomy)
                .economyIncome(economyIncome)
                .build();
    }

    private int calculateHowManyEconomyUpgradeToPremiumPossible(int freePremium, int economyAvailable, int economyIterestsCnt){
        return min(freePremium, max(economyIterestsCnt-economyAvailable, 0));
    }

    private int checkIfLeftAnyPremiumRoom(int initialSizeOfInterests, int rooms, int restRooms){
       return rooms - (initialSizeOfInterests - restRooms);
    }

    private BigDecimal calculateOccupacyForAvailable(int available, LinkedList<BigDecimal> interests){
        BigDecimal sum = BigDecimal.ZERO;
        for(int i=0; i<available; i++){
            if(!interests.isEmpty()){
                sum = sum.add(interests.removeFirst());
            }
        }
        return sum;
    }

    private boolean checkPremiumCandidate(BigDecimal value){
        return value.floatValue() >= 100;
    }
}
