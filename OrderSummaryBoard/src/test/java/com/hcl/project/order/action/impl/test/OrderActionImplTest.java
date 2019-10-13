package com.hcl.project.order.action.impl.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.hcl.project.order.action.OrderAction;
import com.hcl.project.order.action.impl.OrderActionImpl;
import com.hcl.project.order.customException.GenericOrderException;
import com.hcl.project.order.model.Customer;
import com.hcl.project.order.model.Order;
import com.hcl.project.order.model.Product;

@RunWith(MockitoJUnitRunner.class)
public class OrderActionImplTest 
{
	private OrderAction orderAction = new OrderActionImpl();
	
	@Test
	public void registerOrderTest() throws GenericOrderException
	{
		Order order = new Order(1234, 200, "BUY", new Date(), new Product(12, "Option", new BigDecimal(400)), new Customer(12, "Citi"));
		String expected = "Order Registered for User:"+1234+" for quantity:"+200+" at price:"+400;
		String actual = orderAction.registerOrder(order);
		assertEquals(expected, actual);	
	}
	
	@Test(expected = GenericOrderException.class)
	public void registerOrderForExceptionTest() throws GenericOrderException
	{
		orderAction.registerOrder(null);
	}
	
	
	@Test
	public void cancelOrderTest() throws GenericOrderException
	{
		Order order = new Order(1234, 200, "BUY", new Date(), new Product(12, "Option", new BigDecimal(400)), new Customer(12, "Citi"));
		orderAction.registerOrder(order);
		String expected = "Order Cancelled for User with ID"+1234+" for quantity:"+200+" at price:"+400;
		String actual = orderAction.cancelOrder(order);
		assertEquals(expected, actual);	
	}
	
	@Test(expected = GenericOrderException.class)
	public void cancelOrderForExceptionTest() throws GenericOrderException
	{
		orderAction.cancelOrder(null);
	}
	
	@Test
	public void getOrderSummaryTest() throws GenericOrderException
	{
		Order order = new Order(1234, 200, "BUY", new Date(), new Product(12, "Option", new BigDecimal(400)), new Customer(12, "Citi"));
		orderAction.registerOrder(order);
		List<String> orderSummary = orderAction.getOrderSummary();
		assertEquals(1,orderSummary.size());
		
	}
}
