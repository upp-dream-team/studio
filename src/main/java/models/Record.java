package models;

import java.util.Date;

public class Record extends Selling {

	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public float getPrice() {
		throw new UnsupportedOperationException(
				"[Exception] Trying to call getPrice() method on the instance of Record class");
	}

	@Override
	public int getPeriod() {
		throw new UnsupportedOperationException(
				"[Exception] Trying to call getPeriod() method on the instance of Record class");
	}

}
