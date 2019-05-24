package b2bf.components;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import b2bf.es.ProductESRepository;

@Configuration
@Slf4j
class SeedDB {
  
  @Bean
  CommandLineRunner initProduct(ProductRepository repository, FlagRepository frepo, ElasticsearchTemplate elasticsearchTemplate, ProductESRepository pEsRepo) {
	  log.info("Seeding FlagDB ");
	  Flag f1=new Flag("vegan");
	  f1= frepo.save(f1);
	  Flag f2 = new Flag("lactose-free");
	  f2 = frepo.save(f2);
	  
	  log.info("Seeding Product DB");
	  Product p1 = new Product("vnd123", "burger", "vegan burger yuuck", "", 1.5f, f1);
	  p1 = repository.save(p1);
	  Product p2 = new Product("vnd124", "ice cream", "lactose free icecream",
    		  "https://s3-us-west-2.amazonaws.com/elasticbeanstalk-us-west-2-714284225537/products/Selection_034.png",
    		  2.5f, f2);
      p2 = repository.save(p2);
      
      log.info("seeding elasticsearch index");
      IndexQuery indexQuery = new IndexQueryBuilder()
              .withIndexName("product").withObject(p1)
              .withType("product").build();

      elasticsearchTemplate.index(indexQuery);
      indexQuery.setObject(p2);
      elasticsearchTemplate.index(indexQuery);
    return args -> {};
  }
}