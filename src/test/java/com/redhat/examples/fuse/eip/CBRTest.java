package com.redhat.examples.fuse.eip;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class CBRTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/cbr-camel-context.xml";
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:aaa").to("mock:aaa");
                from("direct:bbb").to("mock:bbb");
                from("direct:invalid").to("mock:invalid");
            }
        };
    }

    @Test
    public void aaa() throws Exception {
        getMockEndpoint("mock:aaa").expectedMessageCount(1);
        getMockEndpoint("mock:bbb").expectedMessageCount(0);
        getMockEndpoint("mock:invalid").expectedMessageCount(0);

        template.sendBody("direct:in", Resources.toString(getClass().getResource("/cbr/aaa.xml"), Charsets.UTF_8));

        assertMockEndpointsSatisfied();
    }

    @Test
    public void bbb() throws Exception {
        getMockEndpoint("mock:aaa").expectedMessageCount(0);
        getMockEndpoint("mock:bbb").expectedMessageCount(1);
        getMockEndpoint("mock:invalid").expectedMessageCount(0);

        template.sendBody("direct:in", Resources.toString(getClass().getResource("/cbr/bbb.xml"), Charsets.UTF_8));

        assertMockEndpointsSatisfied();
    }

    @Test
    public void invalid() throws Exception {
        getMockEndpoint("mock:aaa").expectedMessageCount(0);
        getMockEndpoint("mock:bbb").expectedMessageCount(0);
        getMockEndpoint("mock:invalid").expectedMessageCount(1);

        template.sendBody("direct:in", Resources.toString(getClass().getResource("/cbr/invalid.xml"), Charsets.UTF_8));

        assertMockEndpointsSatisfied();
    }

}
