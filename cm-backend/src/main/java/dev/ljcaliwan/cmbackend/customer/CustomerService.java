package dev.ljcaliwan.cmbackend.customer;

import dev.ljcaliwan.cmbackend.exception.DuplicateResourceException;
import dev.ljcaliwan.cmbackend.exception.RequestValidationException;
import dev.ljcaliwan.cmbackend.exception.ResourceNotFoundException;
import dev.ljcaliwan.cmbackend.request.CustomerRegistrationRequest;
import dev.ljcaliwan.cmbackend.request.CustomerUpdateRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomer();
    }

    public Customer getCustomer(Long id) {
         return customerDao.selectCustomerById(id).orElseThrow(
                 () -> new ResourceNotFoundException("customer with id [%s] not found".formatted(id))
         );
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        if(customerDao.existsCustomerWithEmail(request.email())) { // check
            throw new DuplicateResourceException("email already taken.");
        }
        customerDao.insertCustomer( // insert
                new Customer(
                        request.name(), request.email(), request.age()
                )
        );
    }

    public void deleteCustomer(Long id) {
        if(!customerDao.existsCustomerWithId(id)) {
            throw new DuplicateResourceException("customer with id [%s] not exists.".formatted(id));
        }
        customerDao.deleteCustomer(id);
    }

    public void updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = getCustomer(id);

        boolean changes = false;

        if(request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            changes = true;
        }
        if(request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            changes = true;
        }
        if(request.email() != null && !request.email().equals(customer.getEmail())) {
            if(customerDao.existsCustomerWithEmail(request.email())) {
                throw new DuplicateResourceException("email already taken.");
            }
            customer.setEmail(request.email());
            changes = true;
        }

        if(!changes) {
            throw new RequestValidationException("no data changes found.");
        }

        customerDao.updateCustomer(customer);
    }
}
