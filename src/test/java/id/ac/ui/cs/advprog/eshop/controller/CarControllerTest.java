package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private Car car;
    private List<Car> carList;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(5);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Honda Brio");
        car2.setCarColor("White");
        car2.setCarQuantity(3);

        carList = new ArrayList<>();
        carList.add(car);
        carList.add(car2);
    }

    @Test
    void testCreateCarPage() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("createCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void testCreateCarPost() throws Exception {
        doReturn(car).when(carService).create(any(Car.class));

        mockMvc.perform(post("/car/createCar")
                        .param("carId", "car-1")
                        .param("carName", "Toyota Avanza")
                        .param("carColor", "Black")
                        .param("carQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));

        verify(carService).create(any(Car.class));
    }

    @Test
    void testCarListPage() throws Exception {
        doReturn(carList).when(carService).findAll();

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", carList));
    }

    @Test
    void testEditCarPage() throws Exception {
        doReturn(car).when(carService).findById("car-1");

        mockMvc.perform(get("/car/edit/car-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editCar"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attribute("car", car));
    }

    @Test
    void testEditCarPost() throws Exception {
        doNothing().when(carService).update(eq("car-1"), any(Car.class));

        mockMvc.perform(post("/car/edit/car-1")
                        .param("carId", "car-1")
                        .param("carName", "Toyota Yaris")
                        .param("carColor", "Red")
                        .param("carQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));

        verify(carService).update(eq("car-1"), any(Car.class));
    }

    @Test
    void testDeleteCarPost() throws Exception {
        doNothing().when(carService).deleteCarById("car-1");

        mockMvc.perform(post("/car/delete/car-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));

        verify(carService).deleteCarById("car-1");
    }
}