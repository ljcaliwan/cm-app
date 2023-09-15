package dev.ljcaliwan.cmbackend.controller;

import dev.ljcaliwan.cmbackend.customer.Customer;
import dev.ljcaliwan.cmbackend.customer.CustomerService;
import dev.ljcaliwan.cmbackend.request.CustomerRegistrationRequest;
import dev.ljcaliwan.cmbackend.request.CustomerUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable("id") Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("{id}")
    public void updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerUpdateRequest request) {
        customerService.updateCustomer(id, request);
    }
}