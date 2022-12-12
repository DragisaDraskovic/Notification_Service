package com.backend.intern.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.backend.intern.dto.MessageDto;
import com.netflix.discovery.shared.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AwsS3ServiceTest {

    @Mock
    private AmazonS3Client s3Client;

    private AwsS3Service s3Service;

    @Test
    public void putObjectToS3() throws IOException {
        s3Service = new AwsS3Service(s3Client);
        MessageDto dto = MessageDto.builder().courtName("court").playerEmail("stefanljubovic").reservationDate(new Date()).build();
        String objectName = getFileName(dto);
        String objectValue = dto.toString();
        File file = new File("." + File.separator + objectName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            writer.println(objectValue);
        }
        var putObjectRequest = new PutObjectRequest("levi9-"+dto.getCourtName(), objectName, file).withCannedAcl(CannedAccessControlList.PublicRead);

        when(s3Client.doesBucketExist("levi9-court")).thenReturn(true);
        when(s3Client.putObject(putObjectRequest)).thenReturn(null);

        boolean passed= s3Service.putObject(dto);
        file.delete();
        Assert.assertTrue(passed);

    }

    private String getFileName(MessageDto dto) {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(dto.getReservationDate());
        System.out.println(date);
        return dto.getPlayerEmail() + "-" + date+".txt";
    }
}
