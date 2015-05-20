package com.redhat.examples.fuse.eip;

import static com.google.common.collect.Lists.*;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class AggregatorTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/aggregator-camel-context.xml";
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:out").to("mock:out");
            }
        };
    }

    @Test
    public void aggregator() throws Exception {
        MockEndpoint out = getMockEndpoint("mock:out");
        out.expectedMessageCount(1);
        out.expectedBodiesReceived(
                newArrayList("AAA", "BBB", "CCC"),
                newArrayList("aaa", "bbb"),
                newArrayList("***"),
                newArrayList("111"));

        template.sendBodyAndHeaders("direct:in", "AAA", headers("A", 3));
        template.sendBodyAndHeaders("direct:in", "BBB", headers("A", 3));
        template.sendBodyAndHeaders("direct:in", "aaa", headers("B", 2));
        template.sendBodyAndHeaders("direct:in", "CCC", headers("A", 3));
        template.sendBodyAndHeaders("direct:in", "111", headers("C", 2)); // timeout
        template.sendBodyAndHeaders("direct:in", "bbb", headers("B", 2));
        template.sendBodyAndHeaders("direct:in", "***", headers("D", 3, true)); // predicate

        Thread.sleep(2000); // For completion timeout

        assertMockEndpointsSatisfied();
    }

    private Map<String, Object> headers(Object aggregateId, Object totalSize) {
        return headers(aggregateId, totalSize, false);
    }

    private Map<String, Object> headers(Object aggregateId, Object totalSize, Object forceComplete) {
        return ImmutableMap.of("aggregateId", aggregateId, "totalSize", totalSize, "forceComplete", forceComplete);
    }

}
