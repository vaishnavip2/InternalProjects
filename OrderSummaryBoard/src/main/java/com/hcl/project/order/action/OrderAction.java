package com.hcl.project.order.action;

import java.util.List;

import com.hcl.project.order.customException.GenericOrderException;
import com.hcl.project.order.model.Order;

/*
 * OrderMethods is an interface which contains method definition 
 * of registerOrder, cancelOrder and OrderSummary.
 */
public interface OrderAction {
	public String registerOrder(Order order) throws GenericOrderException;
	public String cancelOrder(Order order) throws GenericOrderException;
	public List<String> getOrderSummary();

}
