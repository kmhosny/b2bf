package b2bf.components;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
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

import b2bf.utils.S3Client;


@RestController
@RequestMapping(path="/products")
public class ProductController {
  @Autowired
  private Environment configuration;

  private final ProductRepository repository;

  ProductController(ProductRepository repository) {
    this.repository = repository;
  }
  @GetMapping()
  List<Product> all() {
    return repository.findAll();
  }

  @PostMapping()
  Product newProduct(@RequestBody Product newProduct) {
	newProduct.setImageUrl(uploadToS3(newProduct.getImageUrl()));
    return repository.save(newProduct);
  }
  
  @GetMapping("/{id}")
  Product getProduct(@PathVariable String id) {
	UUID uid = UUID.fromString(id);
    return repository.findById(uid).orElseThrow(() -> new ProductNotFoundException(uid));
  }
  
  @DeleteMapping("/{id}")
  void deleteProduct(@PathVariable String id) {
	UUID uid = UUID.fromString(id);
    repository.deleteById(uid);
  }
  
  @PutMapping("/{id}")
  Product replaceEmployee(@RequestBody Product editProduct, @PathVariable String id) {
	UUID uid = UUID.fromString(id);
	
    return repository.findById(uid)
      .map(product -> {
    	  product.setTitle(editProduct.getTitle());
    	  product.setDescription(editProduct.getDescription());
    	  product.setPrice(editProduct.getPrice());  
    	  product.setImageUrl(uploadToS3(editProduct.getImageUrl()));
    	  return repository.save(product);
      }).orElseThrow(() -> new ProductNotFoundException(uid));
  }
  
  String uploadToS3(String imageData) {
	if(imageData==null)
		return null;
	String img = imageData;
	String imgUrl=S3Client.uploadDataURIImage(configuration.getProperty("aws.bucketName"),
		"products/"+UUID.randomUUID(), img);
	return imgUrl;
  }
}
