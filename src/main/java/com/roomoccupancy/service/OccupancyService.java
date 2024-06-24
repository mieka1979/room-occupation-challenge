package com.roomoccupancy.service;

import com.roomoccupancy.dto.OccupancyDetails;
import java.util.Date;

public interface OccupancyService {

    OccupancyDetails getOccupancyForDate(int premiumRooms, int economyRooms, Date date);
    OccupancyDetails getOccupancy(int premiumRooms, int economyRooms);
}
