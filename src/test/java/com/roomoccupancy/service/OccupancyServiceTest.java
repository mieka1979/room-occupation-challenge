package com.roomoccupancy.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roomoccupancy.dto.OccupancyDetails;
import com.roomoccupancy.model.GuestInterest;
import com.roomoccupancy.repository.GuestInterestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OccupancyServiceTest {

    @Autowired
    private OccupancyService occupancyService;

    @MockBean
    private GuestInterestRepository guestInterestRepository;

    private String input = "" +
            "[ { \"price\" : 23}," +
            "{ \"price\" : 45}," +
            "{ \"price\" : 155}," +
            "{ \"price\" : 374}," +
            "{ \"price\" : 22}," +
            "{ \"price\" : 99.99}," +
            "{ \"price\" : 100}," +
            "{ \"price\" : 101}," +
            "{ \"price\" : 115}," +
            "{ \"price\" : 209} ]";

    @Test
    public void testGetOccupancy(){
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<GuestInterest>>(){}.getType();
        List<GuestInterest> interests = new Gson().fromJson(input, listType);

        Mockito.when(guestInterestRepository.findByDate(Mockito.any()))
                .thenReturn(interests);
        Mockito.when(guestInterestRepository.findAll())
                .thenReturn(interests);

        assertThat(occupancyService.getOccupancy(3,3))
                .isEqualTo(OccupancyDetails.builder()
                        .economyIncome(new BigDecimal("167.99"))
                        .economyUsage(3)
                        .premiumIncome(new BigDecimal(738))
                        .premiumUsage(3)
                        .build());
        assertThat(occupancyService.getOccupancy(7,5))
                .isEqualTo(OccupancyDetails.builder()
                        .economyIncome(new BigDecimal("189.99"))
                        .economyUsage(4)
                        .premiumIncome(new BigDecimal(1054))
                        .premiumUsage(6)
                        .build());

        assertThat(occupancyService.getOccupancy(2,7))
                .isEqualTo(OccupancyDetails.builder()
                        .economyIncome(new BigDecimal("189.99"))
                        .economyUsage(4)
                        .premiumIncome(new BigDecimal(583))
                        .premiumUsage(2)
                        .build());

        assertThat(occupancyService.getOccupancy(7,1))
                .isEqualTo(OccupancyDetails.builder()
                        .economyIncome(new BigDecimal("45"))
                        .economyUsage(1)
                        .premiumIncome(new BigDecimal("1153.99"))
                        .premiumUsage(7)
                        .build());

        assertThat(occupancyService.getOccupancy(7,0))
                .isEqualTo(OccupancyDetails.builder()
                        .economyIncome(new BigDecimal(0))
                        .economyUsage(0)
                        .premiumIncome(new BigDecimal("1153.99"))
                        .premiumUsage(7)
                        .build());

        assertThat(occupancyService.getOccupancy(0,7))
                .isEqualTo(OccupancyDetails.builder()
                        .economyIncome(new BigDecimal("189.99"))
                        .economyUsage(4)
                        .premiumIncome(BigDecimal.ZERO)
                        .premiumUsage(0)
                        .build());
    }

}
