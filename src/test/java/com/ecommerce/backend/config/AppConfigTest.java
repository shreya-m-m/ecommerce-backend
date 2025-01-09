package com.ecommerce.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class AppConfigTest {
	
	 @Autowired
	 private PasswordEncoder passwordEncoder;

    @Test
    void testCorsConfiguration() {
        // Instantiate the AppConfig and get the CorsConfigurationSource
        AppConfig appConfig = new AppConfig();
        CorsConfigurationSource corsConfigurationSource = appConfig.corsConfigurationSource();

        // Cast the source to UrlBasedCorsConfigurationSource to check registrations
        UrlBasedCorsConfigurationSource urlBasedCorsSource = (UrlBasedCorsConfigurationSource) corsConfigurationSource;

        // Create a mock HttpServletRequest with the desired URL pattern
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        // Retrieve the CORS configuration based on the request URI
        CorsConfiguration corsConfig = urlBasedCorsSource.getCorsConfiguration(request);

        // Define the expected CORS configuration
        CorsConfiguration expectedCorsConfig = new CorsConfiguration();
        expectedCorsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://trendinsta.vercel.app"));
        expectedCorsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","OPTIONS"));
        expectedCorsConfig.setAllowedHeaders(Arrays.asList("*"));
        expectedCorsConfig.setExposedHeaders(Arrays.asList("Authorization"));

        // Print the expected and actual values to the console
        System.out.println("Expected CORS Configuration:");
        System.out.println("Allowed Origins: " + expectedCorsConfig.getAllowedOrigins());
        System.out.println("Allowed Methods: " + expectedCorsConfig.getAllowedMethods());
        System.out.println("Allowed Headers: " + expectedCorsConfig.getAllowedHeaders());
        System.out.println("Exposed Headers: " + expectedCorsConfig.getExposedHeaders());

        System.out.println("\nActual CORS Configuration:");
        System.out.println("Allowed Origins: " + corsConfig.getAllowedOrigins());
        System.out.println("Allowed Methods: " + corsConfig.getAllowedMethods());
        System.out.println("Allowed Headers: " + corsConfig.getAllowedHeaders());
        System.out.println("Exposed Headers: " + corsConfig.getExposedHeaders());

        // Assert that the actual CORS configuration matches the expected values
        assertEquals(expectedCorsConfig.getAllowedOrigins(), corsConfig.getAllowedOrigins());
        assertEquals(expectedCorsConfig.getAllowedMethods(), corsConfig.getAllowedMethods());
        assertEquals(expectedCorsConfig.getAllowedHeaders(), corsConfig.getAllowedHeaders());
        assertEquals(expectedCorsConfig.getExposedHeaders(), corsConfig.getExposedHeaders());
    }
    
    @Test
    void testPasswordEncoder() {
    	 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  

    	    String password = "testPassword";
    	    String encodedPassword = passwordEncoder.encode(password);
    	    assertNotEquals(password, encodedPassword);
    	    assertTrue(passwordEncoder.matches(password, encodedPassword));
    	}
}
