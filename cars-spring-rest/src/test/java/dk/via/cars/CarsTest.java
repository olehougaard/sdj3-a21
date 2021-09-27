package dk.via.cars;

import dk.via.web_service.CarsImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class CarsTest {
    private CarsImpl cars;

    @Before
    public void setUp() {
        cars = new CarsImpl();
    }

    @Test
    public void createReturnsCar() {
        Car request = ObjectFactory.createCarDTO("ABC", "Ford", 2020, ObjectFactory.createMoneyDTO(new BigDecimal(20000), "DKK"));
        Car response = cars.create(request);
        assertEquals("ABC", response.getLicenseNumber());
    }
}
