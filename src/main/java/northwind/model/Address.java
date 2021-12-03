package northwind.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Address {

	private String phone;

	private String address;

	private String city;

	private String state;

	private String zip;

	private String country;
	
}
