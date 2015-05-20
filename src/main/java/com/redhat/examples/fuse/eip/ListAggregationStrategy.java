package com.redhat.examples.fuse.eip;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class ListAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            List<String> body = new ArrayList<>();
            body.add(newExchange.getIn().getBody(String.class));
            newExchange.getIn().setBody(body);
            return newExchange;
        }

        @SuppressWarnings("unchecked")
        List<String> body = oldExchange.getIn().getBody(List.class);
        body.add(newExchange.getIn().getBody(String.class));
        return oldExchange;
    }
}
