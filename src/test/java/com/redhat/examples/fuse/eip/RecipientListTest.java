package com.redhat.examples.fuse.eip;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class RecipientListTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/recipient-list-camel-context.xml";
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:aaa").to("mock:aaa");
                from("direct:bbb").to("mock:bbb");
                from("direct:ccc").to("mock:ccc");
            }
        };
    }

    @Test
    public void recipientList() throws Exception {
        getMockEndpoint("mock:aaa").expectedMessageCount(1);
        getMockEndpoint("mock:bbb").expectedMessageCount(1);
        getMockEndpoint("mock:ccc").expectedMessageCount(1);

        template.sendBody("direct:in", "TEST");

        assertMockEndpointsSatisfied();
    }

}
