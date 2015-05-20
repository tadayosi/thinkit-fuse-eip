package com.redhat.examples.fuse.eip;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class AddressSetter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader("addresses", "direct:aaa,direct:bbb,direct:ccc");
    }

}
