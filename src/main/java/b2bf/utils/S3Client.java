package b2bf.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.Getter;
import lombok.Setter;

public class S3Client {
	
	private static AmazonS3 s3client = null;
	private static S3Client singelton = null;
	
	private S3Client() {
		AWSConfig awsConfig = SpringContext.getBean(AWSConfig.class);
		AWSCredentials credentials = new BasicAWSCredentials(
				  awsConfig.getAccessKey(), 
				  awsConfig.getSecretKey()	
				);
		S3Client.s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_WEST_2)
				  .build();
	}
	
	public static AmazonS3 getAmazonS3Client() {
		if(singelton == null)
			singelton = new S3Client();
		return singelton.s3client;
		
	}
	
	public static String uploadDataURIImage(String bucket, String key, String DataURIimage) {
		String[] split = DataURIimage.split(",");
		if(split.length!= 2)
			return "";
		byte[] decodedImage = Base64.getDecoder().decode(split[1]);
		InputStream imageStream = new ByteArrayInputStream(decodedImage);
		ObjectMetadata om = new ObjectMetadata();
		String contentType = split[0].split(";")[0].split(":")[1];
		om.setContentType(contentType);
		key+="."+contentType.split("/")[1];
		S3Client.getAmazonS3Client().putObject(bucket, key, imageStream, om);
		return S3Client.getAmazonS3Client().getUrl(bucket, key).toString();
	}
}
