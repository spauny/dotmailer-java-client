package org.spauny.joy.dotmailer.vo.api;

import java.util.List;
import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class AccountInfo {
    private Long id;
    private List<DMProperty> properties;
}
