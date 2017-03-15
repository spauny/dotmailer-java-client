package com.lindar.dotmailer.vo.api;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class SuppressedContact {
    private Contact suppressedContact;
    private Date dateRemoved;
    private String reason;
}
