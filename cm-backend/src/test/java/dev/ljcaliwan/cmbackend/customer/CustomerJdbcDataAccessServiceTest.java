package dev.ljcaliwan.cmbackend.customer;

import dev.ljcaliwan.cmbackend.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJdbcDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJdbcDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJdbcDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void itShouldSelectAllCustomer() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        // When
        List<Customer> customers = underTest.selectAllCustomer();
        // then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void itShouldSelectCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        Optional<Customer> result = underTest.selectCustomerById(id);
        // then
        assertThat(result).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void itShouldReturnEmptyWhenSelectCustomerById() {
        // Given
        Long id = -1L;
        // When
        Optional<Customer> customer = underTest.selectCustomerById(id);
        // then
        assertThat(customer).isEmpty();
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
        List<Customer> customers = underTest.selectAllCustomer();
        assertThat(customers).isNotEmpty();
    }

    @Test
    void itShouldDeleteCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        underTest.deleteCustomer(id);
        // then
        Optional<Customer> deletedCustomer = underTest.selectCustomerById(id);
        assertThat(deletedCustomer).isNotPresent();
    }

    @Test
    void itShouldUpdateCustomerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);
        // When
        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer data
        String newName = FAKER.name().fullName();

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setName(newName);

        underTest.updateCustomer(updatedCustomer);

        // then
        Optional<Customer> result = underTest.selectCustomerById(id);
        assertThat(result).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName); // updated name
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void itShouldUpdateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);
        // When
        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer data
        String newEmail = FAKER.internet().safeEmailAddress();

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setEmail(newEmail);

        underTest.updateCustomer(updatedCustomer);

        // then
        Optional<Customer> result = underTest.selectCustomerById(id);
        assertThat(result).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail); // updated email
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void itShouldUpdateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);
        // When
        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer data
        var newAge = FAKER.random().nextInt(18, 99);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setAge(newAge);

        underTest.updateCustomer(updatedCustomer);

        // then
        Optional<Customer> result = underTest.selectCustomerById(id);
        assertThat(result).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge); // updated age
        });
    }

    @Test
    void itShouldUpdateAllFieldsOfCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);
        // When
        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer data

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setName(FAKER.name().fullName());
        updatedCustomer.setEmail(FAKER.internet().safeEmailAddress());
        updatedCustomer.setAge(FAKER.random().nextInt(18, 99));

        underTest.updateCustomer(updatedCustomer);

        // then
        Optional<Customer> result = underTest.selectCustomerById(id);
        assertThat(result).isPresent().hasValue(updatedCustomer);
    }

    @Test
    void itShouldNotUpdateCustomerWhenNoChanges() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);
        // When
        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update without changes
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);

        underTest.updateCustomer(updatedCustomer);

        // then
        Optional<Customer> result = underTest.selectCustomerById(id);
        assertThat(result).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void itShouldExistsCustomerWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);
        // When
        boolean exists = underTest.existsCustomerWithEmail(email);
        // then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldReturnFalseWhenCustomerWithEmailNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        // When
        boolean exists = underTest.existsCustomerWithEmail(email);
        // then
        assertThat(exists).isFalse();
    }

    @Test
    void itShouldExistsCustomerWithId() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.random().nextInt(18, 99)
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        boolean exists = underTest.existsCustomerWithId(id);
        // then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldReturnFalseWhenCustomerWithIdNotPresent() {
        // Given
        Long id = -1L;
        // When
        boolean exists = underTest.existsCustomerWithId(id);
        // then
        assertThat(exists).isFalse();
    }
}