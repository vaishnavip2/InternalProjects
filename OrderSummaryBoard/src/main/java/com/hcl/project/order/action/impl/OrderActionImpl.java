package com.hcl.project.order.action.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.project.order.action.OrderAction;
import com.hcl.project.order.customException.GenericOrderException;
import com.hcl.project.order.model.Order;
import com.hcl.project.order.util.OrderConstants;

/** 
* <h1>Implementation of the Live Order Board!</h1> 
* The OrderAction class provides following 
* implementations:
* 1. Register the order.
* 2. cancel the order.
* 3. Show order summary
* TreeMap signifies the elements are stored 
* in sorted order for order type sell and buy
* userOrderDetails map is used to store each 
* order details.
* @throws GenericOrderException: Userdefined exception is 
* used throughout the application. 
* @author  Vaishnavi Anil Potekar 
* @since   2019-10-13 
*/
public class OrderActionImpl implements OrderAction
{
	private static final Logger logger = LoggerFactory.getLogger(OrderActionImpl.class);

	private SortedMap<BigDecimal, Integer> sellOrderRateVsQuantityStore  = new TreeMap<>();
	private SortedMap<BigDecimal, Integer> buyOrderRateVsQuantityStore  = new TreeMap<>(Comparator.reverseOrder());

	private Map<Integer,List<Order>> userOrderDetails = new HashMap<>();
	
	/**
	 * registerOrder is used to register the orders.
	 * It contains Validation check for order type 	.
	 * @param order is the parameter passed to access
	 * quantity ,priceperkg fields and store them in  respective 
	 * TreeMap based on the order type.
	 * Each user detail is stored in userDetails Map which can be 
	 * useful to retrieve orders per user.
	 * @return String This method returns string of registered order.
	 * @author  Vaishnavi Anil Potekar
	 */
	@Override
	public String registerOrder(Order order) throws GenericOrderException
	{
		
		if(order!=null)
		{
			if(!OrderConstants.ORDER_TYPE_SELL.equals(order.getOrderType()) &&
					!OrderConstants.ORDER_TYPE_BUY.equals(order.getOrderType()) ) {
				logger.error(OrderConstants.INVALID_ORDER_TYPE);
				throw new GenericOrderException(OrderConstants.INVALID_ORDER_TYPE);
			}

			//saving for Order Type SELL
			if(OrderConstants.ORDER_TYPE_SELL.equals(order.getOrderType()) )
			{
				if( sellOrderRateVsQuantityStore.containsKey(order.getProduct().getProdPrice())  )
				{
					Integer oldQuantity = sellOrderRateVsQuantityStore.get(order.getProduct().getProdPrice());
					Integer newQuantity = oldQuantity + order.getQuantity();
					sellOrderRateVsQuantityStore.put(order.getProduct().getProdPrice(), newQuantity);
				}
				else
				{
					sellOrderRateVsQuantityStore.put(order.getProduct().getProdPrice(), order.getQuantity());
				}
			}

			//saving for Order Type BUY
			if(OrderConstants.ORDER_TYPE_BUY.equals(order.getOrderType()) )
			{
				if( buyOrderRateVsQuantityStore.containsKey(order.getProduct().getProdPrice()) )
				{
					Integer oldQuantity = buyOrderRateVsQuantityStore.get(order.getProduct().getProdPrice());
					Integer newQuantity = oldQuantity + order.getQuantity();
					buyOrderRateVsQuantityStore.put(order.getProduct().getProdPrice(), newQuantity);
				}
				else
				{
					buyOrderRateVsQuantityStore.put(order.getProduct().getProdPrice(), order.getQuantity());
				}
			}

			if(userOrderDetails.containsKey(order.getUserID()))
			{
				List<Order> orderList = userOrderDetails.get(order.getUserID());
				orderList.add(order);
				userOrderDetails.put(order.getUserID(), orderList);
				return "Order Registered for User:"+order.getUserID()+" for quantity:"+order.getQuantity()+" at price:"+order.getProduct().getProdPrice();
			}
			else
			{
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(order);
				userOrderDetails.put(order.getUserID(), orderList);
				return "Order Registered for User:"+order.getUserID()+" for quantity:"+order.getQuantity()+" at price:"+order.getProduct().getProdPrice();
			}

		}
		else
		{
			logger.error(OrderConstants.INVALID_ORDER);
			throw new GenericOrderException(OrderConstants.INVALID_ORDER);
		}
	}

	
	/**
	 * cancelOrder is used to cancel the order on the basis of userId.
	 * @param order is used which is used in following ways:
	 * 1. To check whether order is null or not
	 * 2. To access userId to check whether it exist in the userDetails Map.
	 * This method validates user order and then remove the particular userID 
	 * and update the userDetails map.
	 * Also, after cancellation of the order, accordingly TreeMap
	 * is updated for order type sell and buy.
	 * @return String This method returns string of UserId, quantity and price.
	 * @author  Vaishnavi Anil Potekar
	 */
	@Override
	public String cancelOrder(Order order) throws GenericOrderException
	{

		if(order!=null)
		{
			if(!OrderConstants.ORDER_TYPE_SELL.equals(order.getOrderType()) &&
					!OrderConstants.ORDER_TYPE_BUY.equals(order.getOrderType()) ) {
				logger.error(OrderConstants.INVALID_ORDER_TYPE);
				throw new GenericOrderException(OrderConstants.INVALID_ORDER_TYPE);
			}

			boolean orderExists=false;
			//Validating User Order before Cancelling and removing from user orders
			if(userOrderDetails.containsKey(order.getUserID()))
			{  
				List<Order> userOrderList = userOrderDetails.get(order.getUserID());
				Iterator<Order> listIterator = userOrderList.listIterator();
				while(listIterator.hasNext())
				{
					Order userOrder = listIterator.next();
					if( userOrder.equals(order) )
					{
						orderExists=true;
						logger.info(OrderConstants.VALID_USER_TO_CANCEL);
						listIterator.remove();
						break;

					}
				}
				userOrderDetails.put(order.getUserID(), userOrderList);
			}
			else if(!orderExists)
			{
				logger.error(OrderConstants.USER_ORDER_DOESNOT_EXIST);
				throw new GenericOrderException(OrderConstants.USER_ORDER_DOESNOT_EXIST);
			}
			else if(!userOrderDetails.containsKey(order.getUserID()))
			{
				logger.error(OrderConstants.USER_ORDER_DOESNOT_EXIST);
				throw new GenericOrderException(OrderConstants.USER_ORDER_DOESNOT_EXIST);
			}


			if(OrderConstants.ORDER_TYPE_SELL.equals(order.getOrderType()) )
			{
				if( sellOrderRateVsQuantityStore.containsKey(order.getProduct().getProdPrice())  )
				{
					Integer oldQuantity = sellOrderRateVsQuantityStore.get(order.getProduct().getProdPrice());
					Integer newQuantity = oldQuantity - order.getQuantity();
					sellOrderRateVsQuantityStore.put(order.getProduct().getProdPrice(), newQuantity);
				}
			}

			if(OrderConstants.ORDER_TYPE_BUY.equals(order.getOrderType()) )
			{
				if( buyOrderRateVsQuantityStore.containsKey(order.getProduct().getProdPrice())  )
				{
					Integer oldQuantity = buyOrderRateVsQuantityStore.get(order.getProduct().getProdPrice());
					Integer newQuantity = oldQuantity - order.getQuantity();
					buyOrderRateVsQuantityStore.put(order.getProduct().getProdPrice(), newQuantity);
				}
			}
		}
		else
		{
			logger.error(OrderConstants.INVALID_ORDER);
			throw new GenericOrderException(OrderConstants.INVALID_ORDER);
		}
		return "Order Cancelled for User with ID"+order.getUserID()+" for quantity:"+order.getQuantity()+" at price:"+order.getProduct().getProdPrice();
	}


	/**
	 * getOrderSummary method displays and returns Summary 
	 * Information of Live Orders
	 * @return List This returns list of order summary .
	 * @author  Vaishnavi Anil Potekar
	 */
	@Override
	public List<String> getOrderSummary()
	{
		List<String> orderSummary = new ArrayList<String>();
		for(Entry<BigDecimal, Integer> sellEntry: sellOrderRateVsQuantityStore.entrySet())
		{
			String sellData = "SELL: " + sellEntry.getValue() + " kg for $"+sellEntry.getKey();
			logger.info("SELL: {}kg for ${}", sellEntry.getValue(), sellEntry.getKey());
			orderSummary.add(sellData);
		}
		for(Entry<BigDecimal, Integer> buyEntry: buyOrderRateVsQuantityStore.entrySet())
		{
			String buyData = "BUY: " + buyEntry.getValue() + " kg for $"+buyEntry.getKey();
			logger.info("BUY: {}kg for ${}", buyEntry.getValue(), buyEntry.getKey());
			orderSummary.add(buyData);
		}
		return orderSummary;
	}
}
