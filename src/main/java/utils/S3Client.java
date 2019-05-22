package utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class S3Client {
	private static AmazonS3 s3client = null;
	private static S3Client singelton = null;
	@Value("${aws.accesskey}")
	private static String accesskey;
	@Value("${aws.secretkey}")
	private static String secretkey;
	private S3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(
				  S3Client.accesskey, 
				  S3Client.secretkey
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
		S3Client.getAmazonS3Client().putObject(bucket, key, imageStream, new ObjectMetadata());
		return S3Client.getAmazonS3Client().getUrl(bucket, key).toString();
	}
}
