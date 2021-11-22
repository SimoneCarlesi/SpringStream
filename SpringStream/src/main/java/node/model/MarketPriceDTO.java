package node.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketPriceDTO {
	@JsonProperty("RuoloResponse")
	

	private Long id;
	private Date timestamp;
	private String currency1;
	private String currency2;
	private float last;
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getCurrency1() {
		return currency1;
	}
	public void setCurrency1(String currency1) {
		this.currency1 = currency1;
	}
	public String getCurrency2() {
		return currency2;
	}
	public void setCurrency2(String currency2) {
		this.currency2 = currency2;
	}
	public float getLast() {
		return last;
	}
	public void setLast(float last) {
		this.last = last;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	
	
	
}
