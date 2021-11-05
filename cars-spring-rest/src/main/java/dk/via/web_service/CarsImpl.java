package dk.via.web_service;

import dk.via.cars.CarDAO;
import dk.via.cars.Car;
import dk.via.cars.ws.Cars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarsImpl {
    private final Cars dao;

    @ExceptionHandler({ NullPointerException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNullPointer() {}

    @Autowired
    public CarsImpl(Cars dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<Car> readAll() {
        return dao.readAll();
    }

    @GetMapping(value = "/{license}")
    public Car read(@PathVariable("license") String licenseNumber) {
        Car car = dao.read(licenseNumber);
        if (car == null) throw new NullPointerException();
        return car;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Car create(@RequestBody Car car)  {
        try {
            return dao.create(car.getLicenseNumber(), car.getModel(), car.getYear(), car.getPrice());
        } catch(RuntimeException e) {
            if (e.getMessage().contains("duplicate key"))
                throw new DuplicateKeyException(e);
            else
                throw e;
        }
    }

    @DeleteMapping(value = "/{license}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("license") String licenseNumber) {
        dao.delete(licenseNumber);
    }
}
