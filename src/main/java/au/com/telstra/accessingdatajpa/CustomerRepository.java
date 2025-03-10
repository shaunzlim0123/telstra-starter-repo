package au.com.telstra.accessingdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

  List<Customer> findByEmail(String customerEmail);

  Customer findById(long id);
}