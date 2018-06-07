package com.lindar.dotmailer.vo.api;

import lombok.Data;

import java.time.Instant;

@Data
public class TransactionalEmailStatistics {
    private Instant startDate;
    private Instant endDate;
    private int numSent;
    private int numDelivered;
    private int numOpens;
    private int numClicks;
    private int numIspComplaints;
    private int numBounces;
}
