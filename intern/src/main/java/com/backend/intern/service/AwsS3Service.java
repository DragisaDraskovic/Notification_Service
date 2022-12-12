package com.backend.intern.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.backend.intern.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;


@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3Client s3Client;

    private void createS3IfNotExists(String courtName) {

        if(s3Client.doesBucketExist("levi9-"+courtName)) {
            return;
        }
        s3Client.createBucket("levi9-"+courtName);
    }


    public boolean putObject(MessageDto dto) throws IOException {
        String objectName = getFileName(dto);
        String objectValue = dto.toString();

        File file = new File("." + File.separator + objectName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            writer.println(objectValue);
            createS3IfNotExists(dto.getCourtName());
            var putObjectRequest = new PutObjectRequest("levi9-"+dto.getCourtName(), objectName, file).withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putObjectRequest);
            file.delete();
            return true;
        } catch (SdkClientException e) {
            throw e;
        }
    }

    private String getFileName(MessageDto dto) {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(dto.getReservationDate());
        System.out.println(date);
        return dto.getPlayerEmail() + "-" + date+".txt";
    }
}
