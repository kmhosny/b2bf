package b2bf;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Product {
	private @Id @GeneratedValue Long id;

	Product() {}

}
