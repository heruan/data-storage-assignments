package org.example.portal;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.example.api.PortNumbers;
import org.example.api.patient.services.PersonalInformationServiceGrpc;

public final class PatientRecordsClient {

    private final PersonalInformationServiceGrpc.PersonalInformationServiceBlockingStub personalInformationService;

    public PatientRecordsClient() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", PortNumbers.PATIENT_RECORDS_PORT).usePlaintext().build();
        Runtime.getRuntime().addShutdownHook(new Thread(channel::shutdown));
        personalInformationService = PersonalInformationServiceGrpc.newBlockingStub(channel);
    }

    public PersonalInformationServiceGrpc.PersonalInformationServiceBlockingStub getPersonalInformationService() {
        return personalInformationService;
    }
}
