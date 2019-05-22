package b2bf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.ObjectMetadata;

import utils.S3Client;

@RestController
@RequestMapping(path="/products")
public class ProductController {
  @Autowired
  private Environment configuration;

  private final ProductRepository repository;

  ProductController(ProductRepository repository) {
    this.repository = repository;
  }
  @GetMapping("/")
  List<Product> all() {
    return repository.findAll();
  }

  @PostMapping("/")
  Product newProduct(@RequestBody Product newProduct) {
	if(newProduct.getImageUrl()!= null) {
		String img = newProduct.getImageUrl();
		newProduct.setImageUrl("");
		repository.save(newProduct);
		String imgUrl=S3Client.uploadDataURIImage(configuration.getProperty("aws.bucketName"), "products/"+newProduct.getId(), img);
		newProduct.setImageUrl(imgUrl);
		repository.save(newProduct);
  	}
 
    return repository.save(newProduct);
  }
}
