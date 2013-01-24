package com.japancuccok.main.cart;

import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.domain.product.ProductMetaData;
import com.japancuccok.common.events.CartItemDelete;
import com.japancuccok.common.events.CartUpdate;
import com.japancuccok.common.wicket.component.BlockUIDecorator;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.japancuccok.common.infrastructure.tools.ImageResourceUtil.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.14.
 * Time: 8:23
 */
public class ProductCheckoutPanel extends Panel {

    private static final long serialVersionUID = -724831719550158739L;
    private int listItemIndex;
    private AmountToOrderModel productAmountToOrder;
    private ChosenSizeModel chosenSize;

    private class AmountToOrderModel implements Serializable{

        private static final long serialVersionUID = 7771935176735174083L;

        private int amountToOrder;

        private AmountToOrderModel(int amountToOrder) {
            this.amountToOrder = amountToOrder;
        }

        public int getProductPriceToPay() {
            return ((Product)getDefaultModelObject()).getPrice()*amountToOrder;
        }

        public int getAmountToOrder() {
            return amountToOrder;
        }

        public void decreaseAmountToOrder() {
            amountToOrder--;
        }

        public void increaseAmountToOrder() {
            amountToOrder++;
        }
    }

    private class ChosenSizeModel implements Serializable {

        private static final long serialVersionUID = 7902841343851770238L;

        private Product.SIZE sizeToOrder;

        private ChosenSizeModel(Product.SIZE sizeToOrder) {
            this.sizeToOrder = sizeToOrder;
        }

        public Product.SIZE getChosenSize() {
            return sizeToOrder;
        }
    }

    public ProductCheckoutPanel(String id, IModel<Product> iModel, int listItemIndex) {
        super(id + listItemIndex, iModel);
        this.listItemIndex = listItemIndex;
        this.chosenSize = new ChosenSizeModel(getCart().get(iModel.getObject().getId()).getChosenSize());
        this.productAmountToOrder = new AmountToOrderModel(getCart().get(iModel.getObject().getId()).getChosenAmount());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(getFirstImage());
        add(new Label("productName", new PropertyModel<Object>(getDefaultModel(), "name")));
        add(getChosenSize());
        TextField productAmount = getProductAmount(new PropertyModel(productAmountToOrder, "amountToOrder"));
        add(productAmount);
        add(getUnitPrice());
        Label priceToPay = getPriceToPay();
        add(priceToPay);
        add(getDeleteLink());
        add(getPlusLink(productAmount, priceToPay));
        add(getMinusLink(productAmount, priceToPay));
   }

    private Label getChosenSize() {
        PropertyModel priceToPayProp = new PropertyModel(chosenSize, "chosenSize");
        Label chosenSizeLabel = new Label("productSize", priceToPayProp) {

            private static final long serialVersionUID = -4792467845765141548L;

            /**
             * {@inheritDoc}
             */
            @Override
            public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
            {
                super.onComponentTagBody(markupStream, openTag);
//                replaceComponentTagBody(markupStream, openTag, "\"" + getDefaultModelObjectAsString() + "\"");
            }

        };
        chosenSizeLabel.setOutputMarkupId(true);
        return chosenSizeLabel;
    }

    private Component getDeleteLink() {
        IndicatingAjaxFallbackLink deleteLink = new IndicatingAjaxFallbackLink("deleteLink", getDefaultModel()) {

            private static final long serialVersionUID = -8054686106943046581L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                getCart().remove(new ProductMetaData((Product) getModelObject()));
                send(getPage(), Broadcast.BREADTH, new CartUpdate(target, null));
                send(getPage(), Broadcast.BREADTH, new CartItemDelete(target, ProductCheckoutPanel.this));
            }
            
            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator()
            {
                return new BlockUIDecorator();
            }

        };
        deleteLink.setOutputMarkupId(true);
        deleteLink.setVersioned(true);
        deleteLink.setMarkupId("deleteLink" + listItemIndex);
        return deleteLink;
    }

    private TextField getProductAmount(PropertyModel amountToOrderProp) {
        final TextField productAmount = new TextField("productAmount", amountToOrderProp) {

            private static final long serialVersionUID = 3326584641440763926L;

            @Override
            public void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("value", getValue() + " db");
            }
        };
        productAmount.add(new AttributeModifier("disabled","true"));
        productAmount.setOutputMarkupId(true);
        productAmount.setMarkupId("productAmount"+listItemIndex);
        return productAmount;
    }

    private AjaxFallbackLink getMinusLink(final TextField productAmount, final Label priceToPay) {
        AjaxFallbackLink minusLink = new AjaxFallbackLink("minusLink", getDefaultModel()) {

            private static final long serialVersionUID = -8054686106943046581L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                if(productAmountToOrder.getAmountToOrder() > 1) {
                    productAmountToOrder.decreaseAmountToOrder();
                }
                ProductMetaData productMetaData = getCart().get(((Product) getModelObject()).getId());
                productMetaData.setChosenAmount(productAmountToOrder.getAmountToOrder());
                getCart().add(productMetaData);
                target.add(productAmount);
                target.add(priceToPay);
                send(getPage(), Broadcast.BREADTH, new CartUpdate(target, null));
            }
        };
        minusLink.setMarkupId("minusLink"+listItemIndex);
        return minusLink;
    }

    private AjaxFallbackLink getPlusLink(final TextField productAmount, final Label priceToPay) {
        AjaxFallbackLink plusLink = new AjaxFallbackLink("plusLink", getDefaultModel()) {

            private static final long serialVersionUID = -8054686106943046581L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                productAmountToOrder.increaseAmountToOrder();
                ProductMetaData productMetaData = getCart().get(((Product) getModelObject()).getId());
                productMetaData.setChosenAmount(productAmountToOrder.getAmountToOrder());
                getCart().add(productMetaData);
                target.add(productAmount);
                target.add(priceToPay);
                send(getPage(), Broadcast.BREADTH, new CartUpdate(target, null));
            }
        };
        plusLink.setVersioned(true);
        plusLink.setMarkupId("plusLink" + listItemIndex);
        return plusLink;
    }

    private TextField getUnitPrice() {
        TextField productUnitPrice = new TextField("productUnitPrice"
                , new Model<Serializable>("Egységár "+((Product) getDefaultModelObject()).getPrice()+" HUF"));
        productUnitPrice.add(new AttributeModifier("disabled", "true"));
        return productUnitPrice;
    }

    private Label getPriceToPay() {
        PropertyModel priceToPayProp = new PropertyModel(productAmountToOrder, "productPriceToPay");
        Label priceToPay = new Label("productPriceToPay", priceToPayProp){

            private static final long serialVersionUID = -5278380944438534817L;

            /**
             * {@inheritDoc}
             */
            @Override
            public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
            {
                replaceComponentTagBody(markupStream, openTag, "Fizetendő " + getDefaultModelObjectAsString() + " HUF");
            }
        };
        priceToPay.setOutputMarkupId(true);
        priceToPay.setMarkupId("productAmountToOrder"+listItemIndex);
        return priceToPay;
    }

    private Component getFirstImage() {
        return getImage(getImageList().get(0), "productImg", this);
    }

    private List<? extends IImage> getImageList() {
        List<IImage> images = new ArrayList<IImage>();
        Product productToOrder = (Product) getDefaultModelObject();
        if(productToOrder.getBinaryImageList() != null) {
            images.addAll(productToOrder.getBinaryImageList());
        }
        if(productToOrder.getUrlImageList() != null) {
            images.addAll(productToOrder.getUrlImageList());
        }
        return images;
    }

    public JapanCuccokSession getJapanSession() {
        return (JapanCuccokSession) getSession();
    }

    public Cart getCart() {
        return getJapanSession().getCart();
    }

    public Product getProductToOrder() {
        return (Product) getDefaultModelObject();
    }
}
