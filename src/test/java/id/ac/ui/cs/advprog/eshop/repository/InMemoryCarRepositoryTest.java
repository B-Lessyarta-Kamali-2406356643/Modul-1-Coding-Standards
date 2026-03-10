package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCarRepositoryTest {

    private InMemoryCarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new InMemoryCarRepository();
    }

    @Test
    void testCreateWithExistingId() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);

        Car result = carRepository.create(car);

        assertEquals("car-1", result.getCarId());
        assertEquals("Toyota", result.getCarName());
        assertEquals("Black", result.getCarColor());
        assertEquals(5, result.getCarQuantity());
    }

    @Test
    void testCreateWithNullIdGeneratesId() {
        Car car = new Car();
        car.setCarId(null);
        car.setCarName("Honda");
        car.setCarColor("White");
        car.setCarQuantity(3);

        Car result = carRepository.create(car);

        assertNotNull(result.getCarId());
        assertFalse(result.getCarId().isBlank());
        assertEquals("Honda", result.getCarName());
        assertEquals("White", result.getCarColor());
        assertEquals(3, result.getCarQuantity());
    }

    @Test
    void testCreateWithBlankIdGeneratesId() {
        Car car = new Car();
        car.setCarId("   ");
        car.setCarName("Suzuki");
        car.setCarColor("Red");
        car.setCarQuantity(2);

        Car result = carRepository.create(car);

        assertNotNull(result.getCarId());
        assertFalse(result.getCarId().isBlank());
        assertEquals("Suzuki", result.getCarName());
        assertEquals("Red", result.getCarColor());
        assertEquals(2, result.getCarQuantity());
    }

    @Test
    void testCreateMultipleCarsWithGeneratedIds() {
        Car car1 = new Car();
        car1.setCarId(null);
        car1.setCarName("Honda");
        car1.setCarColor("White");
        car1.setCarQuantity(3);

        Car car2 = new Car();
        car2.setCarId("   ");
        car2.setCarName("Suzuki");
        car2.setCarColor("Red");
        car2.setCarQuantity(2);

        Car result1 = carRepository.create(car1);
        Car result2 = carRepository.create(car2);

        assertNotNull(result1.getCarId());
        assertNotNull(result2.getCarId());
        assertNotEquals(result1.getCarId(), result2.getCarId());
    }

    @Test
    void testFindAllIfEmpty() {
        List<Car> result = carRepository.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllIfNotEmpty() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("Toyota");
        car1.setCarColor("Black");
        car1.setCarQuantity(5);
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Honda");
        car2.setCarColor("White");
        car2.setCarQuantity(3);
        carRepository.create(car2);

        List<Car> result = carRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("car-1", result.get(0).getCarId());
        assertEquals("car-2", result.get(1).getCarId());
    }

    @Test
    void testFindAllReturnsNewList() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        List<Car> result = carRepository.findAll();
        result.clear();

        assertEquals(1, carRepository.findAll().size());
    }

    @Test
    void testFindByIdFound() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        Car result = carRepository.findById("car-1");

        assertNotNull(result);
        assertEquals("car-1", result.getCarId());
        assertEquals("Toyota", result.getCarName());
        assertEquals("Black", result.getCarColor());
        assertEquals(5, result.getCarQuantity());
    }

    @Test
    void testFindByIdNotFound() {
        Car result = carRepository.findById("not-found");

        assertNull(result);
    }

    @Test
    void testFindByIdOnEmptyRepository() {
        Car result = carRepository.findById("car-1");

        assertNull(result);
    }

    @Test
    void testFindByIdWithNullId() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        Car result = carRepository.findById(null);

        assertNull(result);
    }

    @Test
    void testFindByIdWithEmptyString() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        Car result = carRepository.findById("");

        assertNull(result);
    }

    @Test
    void testFindByIdWithBlankString() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        Car result = carRepository.findById("   ");

        assertNull(result);
    }

    @Test
    void testUpdateFound() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda");
        updatedCar.setCarColor("White");
        updatedCar.setCarQuantity(10);

        carRepository.update("car-1", updatedCar);

        Car result = carRepository.findById("car-1");
        assertNotNull(result);
        assertEquals("car-1", result.getCarId());
        assertEquals("Honda", result.getCarName());
        assertEquals("White", result.getCarColor());
        assertEquals(10, result.getCarQuantity());
    }

    @Test
    void testUpdateNotFound() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda");
        updatedCar.setCarColor("White");
        updatedCar.setCarQuantity(10);

        carRepository.update("car-2", updatedCar);

        Car result = carRepository.findById("car-1");
        assertNotNull(result);
        assertEquals("Toyota", result.getCarName());
        assertEquals("Black", result.getCarColor());
        assertEquals(5, result.getCarQuantity());
    }

    @Test
    void testUpdateOnEmptyRepository() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Honda");
        updatedCar.setCarColor("White");
        updatedCar.setCarQuantity(10);

        carRepository.update("car-1", updatedCar);

        assertTrue(carRepository.findAll().isEmpty());
    }

    @Test
    void testDeleteFound() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        carRepository.delete("car-1");

        assertNull(carRepository.findById("car-1"));
        assertTrue(carRepository.findAll().isEmpty());
    }

    @Test
    void testDeleteNotFound() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(5);
        carRepository.create(car);

        carRepository.delete("car-2");

        assertNotNull(carRepository.findById("car-1"));
        assertEquals(1, carRepository.findAll().size());
    }

    @Test
    void testDeleteOnEmptyRepository() {
        carRepository.delete("car-1");

        assertTrue(carRepository.findAll().isEmpty());
    }
}