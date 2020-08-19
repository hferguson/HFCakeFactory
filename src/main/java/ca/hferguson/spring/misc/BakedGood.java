package ca.hferguson.spring.misc;

import javax.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Entity
@Table(name="catalog")
public class BakedGood {
	@Id
	private final String id;
	private final String name;
	@Column(columnDefinition = "varchar(4000) default ''")
	private final String description;
	@Column(columnDefinition = "varchar(255) default 'http://placehold.it/700x400'")
	private final String image;
	private final double price;
}
