package com.lindar.dotmailer.util;

import java.util.List;
import java.util.function.Consumer;
import com.lindar.dotmailer.vo.api.PersonalisedContact;

/**
 *
 * @author iulian
 */
@FunctionalInterface
public interface PersonalizedContactsProcessFunction<T> extends Consumer<List<PersonalisedContact<T>>> {

    @Override
    void accept(List<PersonalisedContact<T>> contacts);
}
