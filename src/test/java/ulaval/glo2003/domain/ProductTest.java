package ulaval.glo2003.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.utils.TestProductBuilder;

public class ProductTest {

    private final Product validProduct = new TestProductBuilder().build();

    @Test
    public void givenProduct_whenIncrementProductViews_thenIncrementsViews() {
        int oldViews = validProduct.getViews();

        validProduct.incrementViews();

        Assertions.assertEquals(validProduct.getViews(), oldViews + 1);
    }
}
