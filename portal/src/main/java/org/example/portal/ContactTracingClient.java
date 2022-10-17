package org.example.portal;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.example.api.PortNumbers;
import org.example.api.contacttracing.ContactTracingServiceGrpc;

public final class ContactTracingClient {

    private final ContactTracingServiceGrpc.ContactTracingServiceBlockingStub contactTracingService;

    public ContactTracingClient() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", PortNumbers.CONTACT_TRACING_PORT).usePlaintext().build();
        Runtime.getRuntime().addShutdownHook(new Thread(channel::shutdown));
        contactTracingService = ContactTracingServiceGrpc.newBlockingStub(channel);
    }

    public ContactTracingServiceGrpc.ContactTracingServiceBlockingStub getContactTracingService() {
        return contactTracingService;
    }
}
