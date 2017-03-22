package models;

public class License extends Selling {

	private int id;
	private float price;
	private int period;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public int getQuantity() {
		throw new UnsupportedOperationException(
				"[Exception] Trying to call getQuantity() method on the instance of License class");
	}

}
