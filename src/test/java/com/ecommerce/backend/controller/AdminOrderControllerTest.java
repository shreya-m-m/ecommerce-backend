package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.exception.OrderException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.MyOrder;
import com.ecommerce.backend.model.PaymentDetails;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.OrderService;

class AdminOrderControllerTest {

    @InjectMocks
    private AdminOrderController adminOrderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private MyOrder createMockOrder(Long orderId, String orderStatus) {
        User mockUser = new User(1L, "John Doe", "john@example.com", null, null, null, null, null, null, null, null, null);
        Address mockAddress = new Address(1L, "123 Street", "City", "Country", "12345", null, null, mockUser, null);
        PaymentDetails mockPayment = new PaymentDetails();

        return new MyOrder(
            orderId,
            "ORDER" + orderId,
            mockUser,
            null, LocalDateTime.now().minusDays(5),
            LocalDateTime.now(),
            mockAddress,
            mockPayment,
            100.0,
            90,
            10,
            orderStatus,
            3,
            LocalDateTime.now()
        );
    }

    @Test
    void testGetAllOrdersHandler() {
        List<MyOrder> mockOrders = Arrays.asList(
            createMockOrder(1L, "Confirmed"),
            createMockOrder(2L, "Shipped")
        );

        when(orderService.getAllOrders()).thenReturn(mockOrders);

        ResponseEntity<List<MyOrder>> response = adminOrderController.getAllOrdersHandler();

        System.out.println("Expected: 2 orders, Actual: " + response.getBody().size() + " orders");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testConfirmedOrderHandler() throws OrderException {
        MyOrder mockOrder = createMockOrder(1L, "Confirmed");

        when(orderService.confirmOrder(1L)).thenReturn(mockOrder);

        ResponseEntity<MyOrder> response = adminOrderController.ConfirmedOrdeHandler(1L, "Bearer testToken");

        System.out.println("Expected Status: Confirmed, Actual Status: " + response.getBody().getOrderStatus());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Confirmed", response.getBody().getOrderStatus());
        verify(orderService, times(1)).confirmOrder(1L);
    }

    @Test
    void testShippedOrderHandler() throws OrderException {
        MyOrder mockOrder = createMockOrder(2L, "Shipped");

        when(orderService.shippedOrder(2L)).thenReturn(mockOrder);

        ResponseEntity<MyOrder> response = adminOrderController.ShippedOrdeHandler(2L, "Bearer testToken");

        System.out.println("Expected Status: Shipped, Actual Status: " + response.getBody().getOrderStatus());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Shipped", response.getBody().getOrderStatus());
        verify(orderService, times(1)).shippedOrder(2L);
    }

    @Test
    void testDeliveredOrderHandler() throws OrderException {
        MyOrder mockOrder = createMockOrder(3L, "Delivered");

        when(orderService.deliveredOrder(3L)).thenReturn(mockOrder);

        ResponseEntity<MyOrder> response = adminOrderController.DeliverOrdeHandler(3L, "Bearer testToken");

        System.out.println("Expected Status: Delivered, Actual Status: " + response.getBody().getOrderStatus());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Delivered", response.getBody().getOrderStatus());
        verify(orderService, times(1)).deliveredOrder(3L);
    }

//    @Test
//    void testCancelOrderHandler() throws OrderException {
//        MyOrder mockOrder = createMockOrder(4L, "Cancelled");
//
//        when(orderService.cancledOrder(4L)).thenReturn(mockOrder);
//
//        ResponseEntity<MyOrder> response = adminOrderController.CancelOrdeHandler(4L, "Bearer testToken");
//
//        System.out.println("Expected Status: Cancelled, Actual Status: " + response.getBody().getOrderStatus());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("Cancelled", response.getBody().getOrderStatus());
//        verify(orderService, times(1)).cancledOrder(4L);
//    }

    @Test
    void testDeleteOrderHandler() throws OrderException {
        doNothing().when(orderService).deleteOrder(5L);

        ResponseEntity<ApiResponse> response = adminOrderController.DeleteOrdeHandler(5L, "Bearer testToken");

        System.out.println("Expected: MyOrder Deleted Successfully, Actual: " + response.getBody().getMsg());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isStatus());
        assertEquals("MyOrder Deleted Successfully", response.getBody().getMsg());
        verify(orderService, times(1)).deleteOrder(5L);
    }
}
