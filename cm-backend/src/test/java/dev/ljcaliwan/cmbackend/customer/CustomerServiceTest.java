package dev.ljcaliwan.cmbackend.customer;

import dev.ljcaliwan.cmbackend.AbstractTestcontainers;
import dev.ljcaliwan.cmbackend.exception.DuplicateResourceException;
import dev.ljcaliwan.cmbackend.exception.RequestValidationException;
import dev.ljcaliwan.cmbackend.exception.ResourceNotFoundException;
import dev.ljcaliwan.cmbackend.request.CustomerRegistrationRequest;
import dev.ljcaliwan.cmbackend.request.CustomerUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest extends AbstractTestcontainers {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void itShouldGetAllCustomers() {
        // When
        underTest.getAllCustomers();
        // then
        verify(customerDao).selectAllCustomer();
    }

    @Test
    void itShouldGetCustomer() {
        // Given
        Long id = -1L;
        Customer customer = new Customer(
                id,
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.selectCustomerById(id)).willReturn(Optional.of(customer));
        // When
        Customer result = underTest.getCustomer(id);
        // then
        assertThat(result).isEqualTo(customer);
    }

    @Test
    void itShouldThrowExceptionWhenGetCustomerIsEmpty() {
        // Given
        Long id = 1L;

        given(customerDao.selectCustomerById(id)).willReturn(Optional.empty());
        // When
        // then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("customer with id [%s] not found".formatted(id));
    }

    @Test
    void itShouldAddCustomer() {
        // Given
        CustomerRegistrationRequest customer = new CustomerRegistrationRequest(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.existsCustomerWithEmail(customer.email())).willReturn(false);
        // When
        underTest.addCustomer(customer);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        // assert
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.age());
    }

    @Test
    void itShouldNotAddCustomerWhenEmailExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        CustomerRegistrationRequest customer = new CustomerRegistrationRequest(
                email,
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.existsCustomerWithEmail(customer.email())).willReturn(true);
        // When
        // then
        assertThatThrownBy(() -> underTest.addCustomer(customer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email already taken.");

        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void itShouldDeleteCustomer() {
        // Given
        Long id = 1L;

        given(customerDao.existsCustomerWithId(id)).willReturn(true);
        // When
        underTest.deleteCustomer(id);
        // then
        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void itShouldThrowExceptionWhenDeletingCustomerByIdNotExists() {
        // Given
        Long id = 1L;

        given(customerDao.existsCustomerWithId(id)).willReturn(false);
        // When
        // then
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("customer with id [%s] not exists.".formatted(id));

        verify(customerDao, never()).deleteCustomer(id);
    }

    @Test
    void itShouldUpdateAllCustomerFields() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(
                id,
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.selectCustomerById(id)).willReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );

        given(customerDao.existsCustomerWithEmail(request.email())).willReturn(false);
        // When
        underTest.updateCustomer(id, request);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        // assert
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void itShouldUpdateCustomerName() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(
                id,
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.selectCustomerById(id)).willReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                FAKER.name().fullName(),
               null,
                null
        );
        // When
        underTest.updateCustomer(id, request);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        // assert
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void itShouldUpdateCustomerEmail() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(
                id,
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.selectCustomerById(id)).willReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null,
                FAKER.internet().safeEmailAddress(),
                null
        );
        given(customerDao.existsCustomerWithEmail(request.email())).willReturn(false);
        // When
        underTest.updateCustomer(id, request);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        // assert
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void itShouldUpdateCustomerAge() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(
                id,
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.selectCustomerById(id)).willReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null,
                null,
                FAKER.random().nextInt(19, 99)
        );
        // When
        underTest.updateCustomer(id, request);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        // assert
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void itShouldThrowExceptionWhenUpdatingCustomerWithExistingEmail() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(
                id,
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.selectCustomerById(id)).willReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null,
                FAKER.internet().safeEmailAddress(),
                null
        );
        given(customerDao.existsCustomerWithEmail(request.email())).willReturn(true);
        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email already taken.");
        // then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void itShouldThrowExceptionWhenCustomerUpdateHasNoChanges() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(
                id,
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.random().nextInt(18, 99)
        );
        given(customerDao.selectCustomerById(id)).willReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("no data changes found.");
        // then
        verify(customerDao, never()).updateCustomer(any());
    }
}