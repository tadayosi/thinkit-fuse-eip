package com.redhat.examples.fuse.eip;

import java.util.HashSet;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class SetAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            Set<String> body = new HashSet<>();
            body.add(newExchange.getIn().getBody(String.class));
            newExchange.getIn().setBody(body);
            return newExchange;
        }

        @SuppressWarnings("unchecked")
        Set<String> body = oldExchange.getIn().getBody(Set.class);
        body.add(newExchange.getIn().getBody(String.class));
        return oldExchange;
    }
}
