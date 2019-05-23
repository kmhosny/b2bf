package b2bf.components;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(UUID id) {
		super("Could not find employee " + id);
	}
}
