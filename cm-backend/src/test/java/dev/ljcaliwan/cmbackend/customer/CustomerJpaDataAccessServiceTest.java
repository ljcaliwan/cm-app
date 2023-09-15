package dev.ljcaliwan.cmbackend.customer;

import dev.ljcaliwan.cmbackend.AbstractTestcontainers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJpaDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJpaDataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSelectAllCustomer() {
        // When
        underTest.selectAllCustomer();
        // then
        verify(customerRepository).findAll();
    }

    @Test
    void itShouldSelectCustomerById() {
        // Given
        Long id = -1L;
        // When
        underTest.selectCustomerById(id);
        // then
        verify(customerRepository).findById(id);
    }

    @Test
    void itShouldInsertCustomer() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        // When
        underTest.insertCustomer(customer);
        // then
        verify(customerRepository).save(customer);
    }

    @Test
    void itShouldDeleteCustomer() {
        // Given
        Long id = -1L;
        // When
        underTest.deleteCustomer(id);
        // then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void itShouldUpdateCustomer() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        // When
        underTest.updateCustomer(customer);
        // then
        verify(customerRepository).save(customer);
    }

    @Test
    void itShouldExistsCustomerWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        // When
        underTest.existsCustomerWithEmail(email);
        // then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void itShouldExistsCustomerWithId() {
        // Given
        Long id = -1L;
        // When
        underTest.existsCustomerWithId(id);
        // then
        verify(customerRepository).existsCustomerById(id);
    }
}