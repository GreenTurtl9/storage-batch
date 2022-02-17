package com.bsaoudi.batch.entitiesrepository;

import com.bsaoudi.batch.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
