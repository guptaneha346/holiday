package com.holiday.bank;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.findify.s3mock.S3Mock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class AwsConfig {


    private void beforeClass() {
      S3Mock api = new S3Mock.Builder().withPort(9090).withInMemoryBackend().build();
       api.start();
  }

    private AmazonS3 amazonS3Client() {
        this.beforeClass();
        EndpointConfiguration endpoint = new EndpointConfiguration("http://localhost:9090", "us-west-2");
        AmazonS3Client client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();

        client.createBucket("testbucket");
        client.putObject("testbucket", "holiday" , new File("holiday.json"));
        return client;
    }

    public String gets3Json() {
        AmazonS3 s3 = this.amazonS3Client();
        S3Object s3Object = s3.getObject(new GetObjectRequest("testbucket", "holiday"));
        InputStream objectData = s3Object.getObjectContent();
        String result = new BufferedReader(new InputStreamReader(objectData))
                .lines().parallel().collect(Collectors.joining("\n"));
        return result;
    }

        public List<Holiday> getHolidayList() throws ParseException {
            String s3Json = this.gets3Json();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(s3Json);
            JSONArray array = (JSONArray) obj;
            List<Holiday> holidayList = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                HashMap jsonObject = (HashMap) ((JSONObject) array.get(i));
                Holiday holidays = Holiday.builder().day((String) jsonObject.get("day"))
                        .date(LocalDate.parse((CharSequence) jsonObject.get("date"), DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .description((String) jsonObject.get("description"))
                        .timezone((List<String>) jsonObject.get("timezone"))
                        .build();
                holidayList.add(holidays);
            }
            return holidayList;
        }

}
