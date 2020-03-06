package com.void_lhf.wmsp;

public class SearchListItem
{
	private String itemName;
	private String itemDescription;
	private String itemUrl;
	private String itemPassword;

	public SearchListItem(String itemName, String itemDescription, String itemUrl, String itemPassword)
	{
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.itemUrl = itemUrl;
		this.itemPassword = itemPassword;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	public String getItemName()
	{
		return itemName;
	}

	public void setItemDescription(String itemDescription)
	{
		this.itemDescription = itemDescription;
	}

	public String getItemDescription()
	{
		return itemDescription;
	}

	public void setItemUrl(String itemUrl)
	{
		this.itemUrl = itemUrl;
	}

	public String getItemUrl()
	{
		return itemUrl;
	}

	public void setItemPassword(String itemPassword)
	{
		this.itemPassword = itemPassword;
	}

	public String getItemPassword()
	{
		return itemPassword;
	}

	
}
