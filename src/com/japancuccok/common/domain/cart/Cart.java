package com.japancuccok.common.domain.cart;

import com.googlecode.objectify.annotation.*;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.domain.product.ProductMetaData;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 20:58
 */
@Entity
@Cache
public final class Cart implements Serializable {

    private static final long serialVersionUID = -3457581813681931729L;

    transient private static final Logger logger = Logger.getLogger(Cart.class.getName());

    static {
        logger.info("Starting static initialization of Cart...");
    }

    @Id
    private Long id;

    @Serialize
    @Load
    private Map<Long, ProductMetaData> productMap;

    private int total = 0;

    public Cart() {
        logger.info("Starting Cart object construction...");
        productMap = Collections.synchronizedMap(new HashMap<Long, ProductMetaData>());
        logger.info("The actual product list is ["+ productMap.values() +"]");
    }
    
    public Long getId() {
        return id;
    }

    public List<Product> getProducts() {
        List<Product> result;
        synchronized (productMap) {
            if(productMap == null) {
                result = Collections.unmodifiableList(new ArrayList<Product>());
            } else {
                logger.info("The actual product list is ["+ productMap.values() +"]");
                List<Product> temp = new ArrayList<Product>();
                for(ProductMetaData productMetaData : productMap.values()) {
                    temp.add(productMetaData.getProduct());
                }
                result = Collections.unmodifiableList(temp);
            }
        }
        return result;
    }

    public List<Long> getProductIds() {
        List<Long> result;
        synchronized (productMap) {
            if(productMap == null) {
                result = Collections.unmodifiableList(new ArrayList<Long>());
            } else {
                logger.info("The actual product ID list is ["+ productMap.keySet() +"]");
                result = Collections.unmodifiableList(new ArrayList<Long>(productMap.keySet()));
            }
        }
        return result;
    }

    public void add(ProductMetaData productMetaData) {
        synchronized (productMap) {
            if(productMap != null && productMetaData.getProduct() != null) {
                logger.info(this.toString()+" The following product was added to the cart: " +
                        "["+productMetaData.toString()+"]");
                productMap.put(productMetaData.getProduct().getId(), productMetaData);
                logger.info(productMap.toString());
            }
        }
    }

    public void remove(ProductMetaData productMetaData) {
        synchronized (productMap) {
            if(productMap != null) {
                logger.info(this.toString()+"The following product was removed from the cart: " +
                        "["+productMetaData.toString()+"]");
                productMap.remove(productMetaData.getProduct().getId());
                logger.info(productMap.toString());
            }
        }
    }

    public ProductMetaData get(Long productId) {
        synchronized (productMap) {
            if(productMap != null) {
                return productMap.get(productId);
            }
        }
        return null;
    }

    public void clear() {
        synchronized (productMap) {
            if(productMap != null) {
                for(Long productId : productMap.keySet()) {
                    ProductMetaData productMetaData = productMap.get(productId);
                    productMetaData.setProduct(null);
                    productMap.put(productId, productMetaData);
                }
            }
        }
    }

    public int getTotal() {
        total = 0;
        synchronized (productMap) {
            logger.info(this.toString()+" "+productMap.toString());
            if(productMap != null) {
                for(ProductMetaData productMetaData : productMap.values()) {
                    if(productMetaData != null) {
                        //TODO: this line somehow throws NullPointerException,
                        // it must be thoroughly investigated
                        total += productMetaData.getProduct().getPrice()*productMetaData.getChosenAmount();
                    }
                }
            }
        }
        logger.info("Actual cart total is: "+total);
        return total;
    }

    public void detach() {
        logger.info("Cart detach called");
        synchronized (productMap) {
            if(productMap != null) {
//                for(Product product : productMap.values()) {
//                    if(product != null) {
//                        product.setSessionId(Session.get().getId());
//                        productDao.put(product);
//                        logger.info(product+" DETACHED");
//                    }
//                }
                clear();
                cartDao.put(this);
            }
        }
    }
}
