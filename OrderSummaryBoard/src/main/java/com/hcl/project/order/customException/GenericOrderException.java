package com.hcl.project.order.customException;

/*
 * @throws GenericOrderException class is a userdefined 
 * exception class. 
 * It is called in OrderAction class to handle the exception. 
 */
public class GenericOrderException extends Exception 
{
	private static final long serialVersionUID = 82394209L;

	public GenericOrderException(String message)
	{
		super(message);
	}
}
