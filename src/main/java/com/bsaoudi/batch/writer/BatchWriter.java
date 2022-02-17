package com.bsaoudi.batch.writer;

import com.bsaoudi.batch.dto.ConvertedInputData;
import com.bsaoudi.batch.entities.Product;
import com.bsaoudi.batch.entities.PurchaseDate;
import com.bsaoudi.batch.entities.Purchaser;
import com.bsaoudi.batch.entities.Supplier;
import com.bsaoudi.batch.entitiesrepository.*;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BatchWriter implements ItemWriter<ConvertedInputData> {
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private PurchaserRepository purchaserRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PurchaseDateRepository purchaseDateRepository;
	
	@Autowired
	private OrderRepository commandRepository;

	@Override
	public void write(List<? extends ConvertedInputData> items) throws Exception {
		items.stream().forEach(item -> {
			Supplier supplier = null;
			Purchaser purchaser = null;
			Product product = null;
			PurchaseDate purchaseDate = null;
			if(item.getOrder().getSupplierId() == null) {
				supplier = supplierRepository.save(item.getSupplier());
				item.getOrder().setSupplierId(supplier.getId());
			}
			if(item.getOrder().getPurchaserId() == null) {
				purchaser = purchaserRepository.save(item.getPurchaser());
				item.getOrder().setPurchaserId(purchaser.getId());
			}
			if(item.getOrder().getProductId() == null) {
				product = productRepository.save(item.getProduct());
				item.getOrder().setProductId(product.getId());
			}
			if(item.getOrder().getDateId() == null) {
				purchaseDate = purchaseDateRepository.save(item.getPurchaseDate());
				item.getOrder().setDateId(purchaseDate.getId());
			}
			commandRepository.save(item.getOrder());
		});
	}
	
}
