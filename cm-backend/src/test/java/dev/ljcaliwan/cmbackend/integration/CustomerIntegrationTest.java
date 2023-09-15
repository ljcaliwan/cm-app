package dev.ljcaliwan.cmbackend.integration;

import com.github.javafaker.Faker;
import dev.ljcaliwan.cmbackend.AbstractTestcontainers;
import dev.ljcaliwan.cmbackend.customer.Customer;
import dev.ljcaliwan.cmbackend.request.CustomerRegistrationRequest;
import dev.ljcaliwan.cmbackend.request.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest extends AbstractTestcontainers {

    public static final String CUSTOMER_API_PATH = "/api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void itShouldRegisterCustomer() {
        // create register request object
        Faker FAKER = new Faker();
        CustomerRegistrationRequest registerRequest = new CustomerRegistrationRequest(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure  customer is present
        Customer expectedCustomer = new Customer(
                registerRequest.name(),
                registerRequest.email(),
                registerRequest.age()
        );
        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("Id")
                .contains(expectedCustomer);

        // get customer by id
        Long id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(registerRequest.email()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);

       webTestClient.get()
                .uri(CUSTOMER_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }

    @Test
    void itShouldDeleteCustomer() {
        // create register request object
        Faker FAKER = new Faker();
        CustomerRegistrationRequest registerRequest = new CustomerRegistrationRequest(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(registerRequest.email()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // delete customer
        webTestClient.delete()
                .uri(CUSTOMER_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void itShouldUpdateCustomer() {
        // create register request object
        Faker FAKER = new Faker();
        CustomerRegistrationRequest registerRequest = new CustomerRegistrationRequest(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(registerRequest.email()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer
        String newName = FAKER.name().fullName();
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName,
                null,
                null
        );

        webTestClient.put()
                .uri(CUSTOMER_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(
                id,
                newName,
                registerRequest.email(),
                registerRequest.age()
        );

        assertThat(updatedCustomer).isEqualTo(expectedCustomer);
    }
}
