package ca.hferguson.spring.persistence;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Entity
@Table(name="catalog")
public class ItemEntity {

	@Id
	private final String sku;
	@NonNull
	private final String title;
	@NonNull
	private final BigDecimal price;
	// These are the optional db fields so we can use MVP data.sql
	@Column(columnDefinition = "varchar(4000) default ''")
	private final String description;
	@Column(columnDefinition = "varchar(255) default 'http://placehold.it/700x400'")
	private final String image;
	
	@Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemEntity) {
            ItemEntity other = (ItemEntity) obj;
            return Objects.equals(this.sku, other.sku);
        }

        return false;
    }
	@Override
    public int hashCode() {
        return Objects.hashCode(this.sku);
    }
}
