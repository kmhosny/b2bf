package b2bf.components;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Flag {
	@Getter @Setter private @Id @GeneratedValue Integer id;
	@Getter @Setter private String name;
	@JsonIgnore
	@ManyToMany(mappedBy = "flags")
	@Getter @Setter private Set<Product> products;
	
	public Flag() {
		
	}
	public Flag(String name) {
		this.name = name;
	}

}
