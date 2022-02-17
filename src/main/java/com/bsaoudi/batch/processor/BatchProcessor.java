package com.bsaoudi.batch.processor;

import com.bsaoudi.batch.dto.ConvertedInputData;
import com.bsaoudi.batch.dto.InputData;
import com.bsaoudi.batch.entities.*;
import com.bsaoudi.batch.entitiesrepository.ProductRepository;
import com.bsaoudi.batch.entitiesrepository.PurchaseDateRepository;
import com.bsaoudi.batch.entitiesrepository.PurchaserRepository;
import com.bsaoudi.batch.entitiesrepository.SupplierRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchProcessor implements ItemProcessor<InputData, ConvertedInputData> {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaserRepository purchaserRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseDateRepository purchaseDateRepository;

    @Override
    public ConvertedInputData process(InputData item) throws Exception {

        ConvertedInputData convertedInputData = new ConvertedInputData();
        Order order = new Order();

        Supplier supplier = supplierRepository.findByName(item.getSupplierName());
        Purchaser purchaser = purchaserRepository.findByEmail(item.getPurchaserEmail());
        Product product = productRepository.findByEanCode(item.getProductEanCode());
        PurchaseDate purchaseDate = purchaseDateRepository.findByDate(item.getTransactionDate());

        if (supplier == null) {
            supplier = Supplier.of(null, item.getSupplierName(), item.getSupplierAddress());
            convertedInputData.setSupplier(supplier);
        } else {
            order.setSupplierId(supplier.getId());
        }

        if (purchaser == null) {
            purchaser = Purchaser.of(null, item.getPurchaserFirstName(), item.getPurchaserLastName(), item.getPurchaserEmail());
            convertedInputData.setPurchaser(purchaser);
        } else {
            order.setPurchaserId(purchaser.getId());
        }

        if (product == null) {
            product = Product.of(null, item.getProductName(), item.getProductType(), item.getProductEanCode());
            convertedInputData.setProduct(product);
        } else {
            order.setProductId(product.getId());
        }
        order.setQuantity(item.getProductQuantity());
        order.setAmount(item.getProductAmount() * item.getProductQuantity());

        if (purchaseDate == null) {
            purchaseDate = PurchaseDate.of(null, item.getTransactionDate());
            convertedInputData.setPurchaseDate(purchaseDate);
        } else {
            order.setDateId(purchaseDate.getId());
        }

        convertedInputData.setOrder(order);

        return convertedInputData;
    }

}
