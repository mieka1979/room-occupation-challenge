package com.roomoccupancy.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;


@Getter
@Builder
@EqualsAndHashCode
public class OccupancyDetails {

    int premiumUsage;
    BigDecimal premiumIncome;
    int economyUsage;
    BigDecimal economyIncome;
}
