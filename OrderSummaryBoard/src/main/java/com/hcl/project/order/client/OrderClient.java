package com.hcl.project.order.client;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.project.order.action.OrderAction;
import com.hcl.project.order.action.impl.OrderActionImpl;
import com.hcl.project.order.customException.GenericOrderException;
import com.hcl.project.order.model.Customer;
import com.hcl.project.order.model.Order;
import com.hcl.project.order.model.Product;

public class OrderClient 
{
	private static final Logger logger = LoggerFactory.getLogger(OrderClient.class);
	/** 
	    * This is the main method which makes use of registerOrder
	    *  cancelOrder and getOrderSummary method. 
	    * @param args Unused. 
	    * @return Nothing. 
	    */
	public static void main(String[] args) 
	{
		OrderAction orderAction = new OrderActionImpl();
		
		Order order1 = new Order(1, 1, "SELL", new Date(), new Product(1,"Future", new BigDecimal(300)),new Customer(1,"Citi"));
		Order order2 = new Order(2, 2, "SELL", new Date(), new Product(2,"Forward", new BigDecimal(300)),new Customer(2,"Deutsche"));
		Order order3 = new Order(3, 56, "SELL", new Date(), new Product(3,"Option", new BigDecimal(400)),new Customer(3,"Federal"));
		Order order4 = new Order(4, 9, "SELL", new Date(), new Product(4,"Swap", new BigDecimal(400)),new Customer(4,"Scotia"));
		try
		{
			orderAction.registerOrder(order1);
			orderAction.registerOrder(order2);
			orderAction.registerOrder(order3);
			orderAction.registerOrder(order4);
			List<String> dashBoardMessages =  orderAction.getOrderSummary();
			orderAction.cancelOrder(order4);
			List<String> dashBoardMessagesAfterCancellation =  orderAction.getOrderSummary();
		}
		catch(GenericOrderException exception)
		{
			logger.error("Unable to process user order due to:",exception);
		}
		catch(Exception e)
		{
			logger.error("Exception caught in processing :",e);
		}
	}
}
