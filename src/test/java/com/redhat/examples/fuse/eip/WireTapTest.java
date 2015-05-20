package com.redhat.examples.fuse.eip;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class WireTapTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/wiretap-camel-context.xml";
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:out").to("mock:out");
                from("direct:monitor").to("mock:monitor");
            }
        };
    }

    @Test
    public void wireTap() throws Exception {
        getMockEndpoint("mock:out").expectedMessageCount(1);
        getMockEndpoint("mock:monitor").expectedMessageCount(1);

        template.sendBody("direct:in", "TEST");

        assertMockEndpointsSatisfied();
    }

}
