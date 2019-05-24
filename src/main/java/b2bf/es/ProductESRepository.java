package b2bf.es;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import b2bf.components.Product;

public interface ProductESRepository extends ElasticsearchRepository<Product, UUID> {
}
