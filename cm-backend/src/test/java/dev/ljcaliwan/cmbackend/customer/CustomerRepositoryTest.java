package dev.ljcaliwan.cmbackend.customer;

import dev.ljcaliwan.cmbackend.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace= NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void itShouldExistsCustomerByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.save(customer);
        // When
        boolean result = underTest.existsCustomerByEmail(email);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void itShouldFailsWhenCustomerEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        // When
        boolean result = underTest.existsCustomerByEmail(email);
        // then
        assertThat(result).isFalse();
    }

    @Test
    void itShouldExistsCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.save(customer);

        Long id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        boolean result = underTest.existsCustomerById(id);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void itShouldFailsWhenIdNotPresent() {
        // Given
        Long id = -1L;
        // When
        boolean result = underTest.existsCustomerById(id);
        // then
        assertThat(result).isFalse();
    }
}