package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void createProductPage_returnsCreateProductView_andHasProductModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPost_callsService_andRedirectsToList() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productId", "1")
                        .param("productName", "A")
                        .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service).create(any(Product.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void productListPage_returnsProductListView_andHasProducts() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void editProductPage_whenProductNotFound_redirectsToList() throws Exception {
        when(service.findById("404")).thenReturn(null);

        mockMvc.perform(get("/product/edit/404"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service).findById("404");
        verifyNoMoreInteractions(service);
    }

    @Test
    void editProductPage_whenProductExists_returnsEditProductView_andHasProductModel() throws Exception {
        Product p = new Product();
        p.setProductId("9");
        p.setProductName("Old");
        p.setProductQuantity(1);

        when(service.findById("9")).thenReturn(p);

        mockMvc.perform(get("/product/edit/9"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("product", p));

        verify(service).findById("9");
        verifyNoMoreInteractions(service);
    }

    @Test
    void editProductPost_callsService_andRedirectsToList() throws Exception {
        mockMvc.perform(post("/product/edit/9")
                        .param("productId", "9")
                        .param("productName", "Updated")
                        .param("productQuantity", "99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service).editProduct(eq("9"), any(Product.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteProductPost_callsService_andRedirectsToList() throws Exception {
        mockMvc.perform(post("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service).deleteProduct("1");
        verifyNoMoreInteractions(service);
    }
}