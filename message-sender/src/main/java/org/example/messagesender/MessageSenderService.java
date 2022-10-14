package org.example.messagesender;

import io.grpc.stub.StreamObserver;
import org.example.api.messagesender.services.*;
import org.example.api.time.TimeUtils;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class MessageSenderService extends MessageSenderServiceGrpc.MessageSenderServiceImplBase {

    private final AtomicLong nextQueueNumber = new AtomicLong();

    @Override
    public void sendMessage(SendMessageCommand request, StreamObserver<SendMessageCommandResponse> responseObserver) {
        if (request.getToCount() == 0 && request.getCcCount() == 0 && request.getBccCount() == 0) {
            responseObserver.onNext(SendMessageCommandResponse.newBuilder()
                    .setTimestamp(TimeUtils.wrapInstant(Instant.now()))
                    .setRefused("No recipient specified")
                    .build());
        } else if (request.getSubject().isBlank()) {
            responseObserver.onNext(SendMessageCommandResponse.newBuilder()
                    .setTimestamp(TimeUtils.wrapInstant(Instant.now()))
                    .setRefused("No subject specified")
                    .build());
        } else {
            responseObserver.onNext(SendMessageCommandResponse.newBuilder()
                    .setTimestamp(TimeUtils.wrapInstant(Instant.now()))
                    .setAccepted(MessageQueueNumber.newBuilder().setId(nextQueueNumber.getAndIncrement()).build())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getMessageStatus(MessageStatusQuery request, StreamObserver<MessageStatusQueryResponse> responseObserver) {
        var queueNumber = request.getQueueNumber().getId();
        if (queueNumber > nextQueueNumber.get()) {
            responseObserver.onNext(MessageStatusQueryResponse.newBuilder()
                    .setTimestamp(TimeUtils.wrapInstant(Instant.now()))
                    .setStatus(MessageStatus.UNRECOGNIZED)
                    .build());
        } else {
            responseObserver.onNext(MessageStatusQueryResponse.newBuilder()
                    .setTimestamp(TimeUtils.wrapInstant(Instant.now()))
                    .setStatus(MessageStatus.SENT)
                    .build());
        }
        responseObserver.onCompleted();
    }
}
