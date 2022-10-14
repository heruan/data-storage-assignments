package org.example.messagesender;

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.example.api.PortNumbers;

import java.util.Scanner;

public class MessageSenderApp {
    public static void main(String[] args) throws Exception {
        var server = NettyServerBuilder.forPort(PortNumbers.MESSAGE_SENDER_PORT)
                .addService(new MessageSenderService())
                .build();
        server.start();
        var scanner = new Scanner(System.in);
        System.out.println("Press enter to exit");
        scanner.nextLine();
        server.shutdown();
        server.awaitTermination();
    }
}