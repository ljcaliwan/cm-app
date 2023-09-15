package dev.ljcaliwan.cmbackend.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> selectAllCustomer();
    Optional<Customer> selectCustomerById(Long id);
    void insertCustomer(Customer customer);
    void deleteCustomer(Long id);
    void updateCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    boolean existsCustomerWithId(Long id);
}
