package com.zel92.order.config;

import com.zel92.order.client.InventoryClient;
import com.zel92.order.client.PaymentClient;
import com.zel92.order.client.ProductClient;
import com.zel92.order.client.UserClient;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BeansConfig implements WebMvcConfigurer {

    @Value("${client.inventory.url}")
    private String inventoryUrl;
    @Value("${client.product.url}")
    private String productUrl;
    @Value("${client.user.url}")
    private String userUrl;
    @Value("${client.payment.url}")
    private String paymentUrl;

    @Bean
    public InventoryClient inventoryClient(){
        RestClient restClient = RestClient.builder().baseUrl(inventoryUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(InventoryClient.class);
    }

    @Bean
    public ProductClient productClient(){
        RestClient restClient = RestClient.builder().baseUrl(productUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ProductClient.class);
    }

    @Bean
    public UserClient userClient(){
        RestClient restClient = RestClient.builder().baseUrl(userUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UserClient.class);
    }

    @Bean
    public PaymentClient paymentClient(){
        RestClient restClient = RestClient.builder().baseUrl(paymentUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(PaymentClient.class);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");

            }
        };
    }


    @Bean
    public OpenAPI orderServiceAPI(){
        return new OpenAPI()
                .info(new Info().title("Order Service API")
                        .description("Rest API for Order Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("You can refer to the Order Service Wiki Documentation")
                        .url("https://order-service-dummy-url.com/docs"));
    }
}
