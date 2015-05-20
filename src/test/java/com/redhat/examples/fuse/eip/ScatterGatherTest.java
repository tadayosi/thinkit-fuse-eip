package com.redhat.examples.fuse.eip;

import static com.google.common.collect.Sets.*;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class ScatterGatherTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/scatter-gather-camel-context.xml";
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:gather/out").to("mock:out");
            }
        };
    }

    @Test
    public void aggregator() throws Exception {
        MockEndpoint out = getMockEndpoint("mock:out");
        out.expectedMessageCount(1);
        out.message(0).body().isEqualTo(newHashSet("*** TEST ***", "<<< TEST >>>", "!!! TEST !!!"));

        template.sendBodyAndHeader("direct:scatter/in", "TEST", "aggregateId", "A");
        Thread.sleep(2000);

        assertMockEndpointsSatisfied();
    }

}
