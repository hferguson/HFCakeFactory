package ca.hferguson.spring.persistence;



import javax.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
@Entity
@Table(name="address")
public class AddressEntity {

	@Id
	private final String accountID;
	@Column(columnDefinition="varchar(255) not null")
	private final String street;
	@Column(columnDefinition="varchar(255) not null")
	private final String city;
	
	@Column(columnDefinition="varchar(2) not null")
	private final String stateOrProvince;
	
	@Column(columnDefinition="varchar(7) not null")
	private final String postalCode;
	
	

}
