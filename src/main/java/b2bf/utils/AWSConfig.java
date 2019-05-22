package b2bf.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="aws")
public class AWSConfig {
	private  String accesskey;
	
	private  String secretkey;
	
	public  String getAccessKey() {
		return this.accesskey;
	}
	
	public  String getSecretKey() {
		return this.secretkey;
	}
	
	public  void setAccessKey(String accesskey) {
		System.out.println("access key "+ accesskey);
		this.accesskey = accesskey;
	}
	
	public  void setSecretKey(String secretkey) {
		this.secretkey = secretkey;
	}
}
