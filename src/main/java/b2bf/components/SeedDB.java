package b2bf.components;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Slf4j
class SeedDB {
  
  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE)
  CommandLineRunner initProduct(ProductRepository repository, FlagRepository frepo) {
	  log.info("Preloading " + frepo.save(new Flag("vegan")));
	  log.info("Preloading " + frepo.save(new Flag("lactose-free")));
	  Flag f1 = frepo.findAll().get(0);
	  Flag f2 = frepo.findAll().get(1);
    return args -> {
      log.info("Preloading " + repository.save(new Product("vnd123", "burger", "vegan burger yuuck", "", 1.5f, f1)));
      log.info("Preloading " + repository.save(new Product("vnd124", "ice cream", "lactose free icecream",
    		  "https://s3-us-west-2.amazonaws.com/elasticbeanstalk-us-west-2-714284225537/products/Selection_034.png",
    		  2.5f, f2)));
    };
  }
}