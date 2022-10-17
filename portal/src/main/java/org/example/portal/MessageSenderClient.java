package org.example.portal;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.example.api.PortNumbers;
import org.example.api.messagesender.services.MessageSenderServiceGrpc;

public final class MessageSenderClient {

    private final MessageSenderServiceGrpc.MessageSenderServiceBlockingStub messageSenderService;

    public MessageSenderClient() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", PortNumbers.MESSAGE_SENDER_PORT).usePlaintext().build();
        Runtime.getRuntime().addShutdownHook(new Thread(channel::shutdown));
        messageSenderService = MessageSenderServiceGrpc.newBlockingStub(channel);
    }

    public MessageSenderServiceGrpc.MessageSenderServiceBlockingStub getMessageSenderService() {
        return messageSenderService;
    }
}
