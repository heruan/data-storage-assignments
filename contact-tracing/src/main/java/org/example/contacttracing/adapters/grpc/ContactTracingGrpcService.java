package org.example.contacttracing.adapters.grpc;

import io.grpc.stub.StreamObserver;
import org.example.api.contacttracing.*;
import org.example.contacttracing.ports.ContactTracingPort;
import org.springframework.stereotype.Service;

@Service
public class ContactTracingGrpcService extends ContactTracingServiceGrpc.ContactTracingServiceImplBase {

    private final ContactTracingPort contactTracingPort;

    public ContactTracingGrpcService(ContactTracingPort contactTracingPort) {
        this.contactTracingPort = contactTracingPort;
    }

    @Override
    public void registerPerson(RegisterPersonCommand request, StreamObserver<PersonId> responseObserver) {
        throw new UnsupportedOperationException("Implement call to contactTracingPort");
    }

    @Override
    public void confirmContagion(ConfirmContagionCommand request, StreamObserver<ContagionId> responseObserver) {
        throw new UnsupportedOperationException("Implement call to contactTracingPort");
    }

    @Override
    public void recordPotentialExposure(RecordPotentialExposureCommand request, StreamObserver<PotentialExposureId> responseObserver) {
        throw new UnsupportedOperationException("Implement call to contactTracingPort");
    }

    @Override
    public void isPotentiallyExposed(PotentialExposureQuery request, StreamObserver<PotentialExposureQueryResponse> responseObserver) {
        throw new UnsupportedOperationException("Implement call to contactTracingPort");
    }
}
