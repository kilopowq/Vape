package com.kilopo.vape.api.util;

import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Resources;

import java.util.List;
import java.util.function.Function;

public class ResourcesUtil {

    public static <A, B> Resources<B> map(Resources<A> resources, Function<List<A>, List<B>> func) {
        return new Resources<>(
                func.apply(ImmutableList.copyOf(resources.getContent())));
    }
}
