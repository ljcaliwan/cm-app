package dev.ljcaliwan.cmbackend.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    private static List<Customer> customers; // fake db

    static {
        customers = new ArrayList<>();
        Customer customer1 = new Customer(
                1L,
                "Tacs",
                "tacs@gmail.com",
                22
        );
        customers.add(customer1);
        Customer customer2 = new Customer(
                2L,
                "LJ",
                "lj@gmail.com",
                21
        );
        customers.add(customer2);
    }

    @Override
    public List<Customer> selectAllCustomer() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers
                .stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customers.stream()
                .anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerWithId(Long id) {
        return customers.stream()
                .anyMatch(customer -> customer.getId().equals(id));
    }
}
