package com.roomoccupancy.controller;

import com.roomoccupancy.dto.OccupancyDetails;
import com.roomoccupancy.service.OccupancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class RoomOccupancyController {

    @Autowired
    private OccupancyService occupancyService;

    @GetMapping("/room/occupancy")
    public OccupancyDetails getRoomOccupancy(@RequestParam(value = "premium", defaultValue = "0") int premiumRooms,
                                             @RequestParam(value = "economy", defaultValue = "0") int economyRooms,
                                             @RequestParam(value = "date", required=false) Date date){
        System.out.println("JESTEM!!!!");
        if(date!=null){
           return occupancyService.getOccupancyForDate(premiumRooms, economyRooms, date);
        } else {
            return occupancyService.getOccupancy(premiumRooms, economyRooms);
        }
    }
}
