package org.spauny.joy.dotmailer.util;

import java.util.List;
import java.util.function.Consumer;
import org.spauny.joy.dotmailer.vo.api.PersonalisedContact;

/**
 *
 * @author iulian
 */
@FunctionalInterface
public interface PersonalizedContactsProcessFunction<T> extends Consumer<List<PersonalisedContact<T>>> {

    @Override
    void accept(List<PersonalisedContact<T>> contacts);
}
