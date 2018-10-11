package com.l1.mslab.store.customer.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.l1.mslab.store.customer.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

	List<Customer> findByLastName(String lastName);
}
