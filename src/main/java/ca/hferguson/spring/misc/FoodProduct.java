package ca.hferguson.spring.misc;
/**
 * Leaving this version in but user story calls for a drastically reduced
 * implementation. See BakedGood class for newer implementation
 */
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FoodProduct {

	private final String productType;
	private final String productName;
	private final String description;
	private final long sku;
	private final String imgSmall;
	private final String imgLarge;
	private final float price;
	


}
