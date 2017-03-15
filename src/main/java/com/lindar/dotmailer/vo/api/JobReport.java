/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lindar.dotmailer.vo.api;

import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class JobReport {

    private Integer newContacts;
    private Integer updatedContacts;
    private Integer globallySuppressed;
    private Integer invalidEntries;
    private Integer duplicateEmails;
    private Integer blocked;
    private Integer unsubscribed;
    private Integer hardBounced;
    private Integer softBounced;
    private Integer ispComplaints;
    private Integer mailBlocked;
    private Integer domainSuppressed;
    private Integer pendingDoubleOptin;
    private Integer failures;
}
