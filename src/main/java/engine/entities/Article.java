package engine.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Article {
    
    @Id
    private Long sku;
    private double[] attrs = new double[10];

	public double getAttr(int index) {
		return this.attrs[index];
	}

	public void setAttr(int index, double value) {
		this.attrs[index] = value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("sku:" + sku + ", attr:[");
		for(int i=0;i<attrs.length;i++) {
			sb.append(attrs[i]);
			if(i!=attrs.length-1)
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	public double[] getVector() {
		return attrs;
	}

	public Long getSku() {
		return sku;
	}

	public void setSku(Long sku) {
		this.sku = sku;
		
	}
}