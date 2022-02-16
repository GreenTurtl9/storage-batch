package com.gkemayo.batch.entitiesrepository;

import com.gkemayo.batch.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
