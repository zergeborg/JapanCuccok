package com.japancuccok.common.domain.product;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.17.
 * Time: 0:39
 */
public class ProductMetaData implements Serializable {

    private static final long serialVersionUID = 3028434660569143377L;

    private Product product;
    private Product.SIZE chosenSize;
    private int chosenAmount = 1;

    public ProductMetaData(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product.SIZE getChosenSize() {
        return chosenSize;
    }

    public void setChosenSize(Product.SIZE chosenSize) {
        this.chosenSize = chosenSize;
    }

    public int getChosenAmount() {
        return chosenAmount;
    }

    public void setChosenAmount(int chosenAmount) {
        this.chosenAmount = chosenAmount;
    }

    @Override
    public String toString() {
        return "ProductMetaData{" +
                "product=" + product +
                ", chosenSize=" + chosenSize +
                ", chosenAmount=" + chosenAmount +
                '}';
    }
}

