package b2bf.components;

import java.util.List;
import java.util.UUID;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;

import b2bf.utils.S3Client;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
	@Autowired
	private Environment configuration;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	private final ProductRepository repository;

	ProductController(ProductRepository repository) {
		this.repository = repository;
	}

	@GetMapping()
	List<Product> all(@RequestParam(defaultValue = "*") String keyword) {
		QueryBuilder query = QueryBuilders.boolQuery().should(
				QueryBuilders.queryStringQuery("*" + keyword + "*").lenient(true).field("description").field("title"));
		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(query).build();
		List<Product> products = elasticsearchTemplate.queryForList(build, Product.class);
		return products;
	}

	@PostMapping()
	Product newProduct(@RequestBody Product newProduct) {
		newProduct.setImageUrl(uploadToS3(newProduct.getImageUrl()));
		newProduct = repository.save(newProduct);
		IndexQuery indexQuery = new IndexQueryBuilder().withIndexName("product").withObject(newProduct)
				.withType("product").build();
		elasticsearchTemplate.index(indexQuery);
		return newProduct;
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

		return repository.findById(uid).map(product -> {
			product.setTitle(editProduct.getTitle());
			product.setDescription(editProduct.getDescription());
			product.setPrice(editProduct.getPrice());
			product.setImageUrl(uploadToS3(editProduct.getImageUrl()));
			product = repository.save(product);
			IndexQuery indexQuery = new IndexQueryBuilder().withIndexName("product").withObject(product)
					.withType("product").build();
			elasticsearchTemplate.index(indexQuery);
			return product;
		}).orElseThrow(() -> new ProductNotFoundException(uid));
	}

	String uploadToS3(String imageData) {
		if (imageData == null)
			return null;
		String img = imageData;
		String imgUrl = S3Client.uploadDataURIImage(configuration.getProperty("aws.bucketName"),
				"products/" + UUID.randomUUID(), img);
		return imgUrl;
	}
}
