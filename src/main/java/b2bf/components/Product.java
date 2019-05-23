package b2bf.components;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Product {
	@Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(length = 16)
	@Getter @Setter private UUID id;
	
	@Column(nullable = false)
	@Getter @Setter private String vendorUID;
	
	@Getter @Setter private String title;
	
	@Getter @Setter private String description;
	
	@Column(nullable = false)
	@Getter @Setter private float price;
	
	@Getter @Setter private String imageUrl;
	
	@ManyToMany
	@Getter @Setter private Set<Flag> flags;
	
	@Getter @Setter private Long impressions;
	
	Product() {}
		
	Product(String vendorUID, String title, String description, String imageUrl, float price, Flag f) {
		this.vendorUID = vendorUID;
		this.title = title;
		this.description = description;
		this.imageUrl = imageUrl;
		this.price = price;
		this.impressions = 0l;
		this.flags = new HashSet<Flag>();
		this.flags.add(f);
	}

}
