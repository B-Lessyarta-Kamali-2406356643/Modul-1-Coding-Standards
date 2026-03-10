package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car1;
    private Car car2;

    @BeforeEach
    void setUp() {
        car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("Toyota Avanza");
        car1.setCarColor("Black");
        car1.setCarQuantity(5);

        car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Honda Brio");
        car2.setCarColor("White");
        car2.setCarQuantity(3);
    }

    @Test
    void testCreate() {
        when(carRepository.create(car1)).thenReturn(car1);

        Car result = carService.create(car1);

        assertSame(car1, result);
        verify(carRepository, times(1)).create(car1);
    }

    @Test
    void testFindAll() {
        List<Car> cars = Arrays.asList(car1, car2);
        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        assertSame(cars, result);
        assertEquals("car-1", result.get(0).getCarId());
        assertEquals("car-2", result.get(1).getCarId());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(carRepository.findById("car-1")).thenReturn(car1);

        Car result = carService.findById("car-1");

        assertSame(car1, result);
        assertEquals("car-1", result.getCarId());
        verify(carRepository, times(1)).findById("car-1");
    }

    @Test
    void testUpdate() {
        carService.update("car-1", car1);

        verify(carRepository, times(1)).update("car-1", car1);
    }

    @Test
    void testDeleteCarById() {
        carService.deleteCarById("car-1");

        verify(carRepository, times(1)).delete("car-1");
    }
}