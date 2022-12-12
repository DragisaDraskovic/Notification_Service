package com.backend.intern.service;

import com.backend.intern.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class AwsSQSService {

    private final AwsS3Service awsS3Service;

    @SqsListener(value = "player-notification", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void loadMessageFromSQS(final MessageDto message) throws IOException {
        awsS3Service.putObject(message);
    }
}
