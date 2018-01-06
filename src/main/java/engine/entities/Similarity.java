package engine.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = { @Index(name = "IDX_LEFT", columnList = "sku,similarity") , @Index(name = "IDX_RIGHT", columnList = "similar_sku,similarity") })
public class Similarity {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long sku;
    private long similar_sku;
    private double similarity;
    private double[] attrs = new double[10];
  
    public Similarity() {
    	
    }
    
    public Similarity(long sku, long similar_sku, double similarity) {
    	this.sku = sku;
    	this.similar_sku = similar_sku;
    	this.similarity = similarity;
    }    
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getSku() {
		return sku;
	}
	public void setSku(long sku) {
		this.sku = sku;
	}
	public Long getSimilarSku() {
		return similar_sku;
	}
	public void setSimilarSku(long similar_sku) {
		this.similar_sku = similar_sku;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	public double getAttr(int index) {
		return this.attrs[index];
	}

	public void setAttr(int index, double value) {
		this.attrs[index] = value;
	}	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("sku:" + sku + ", similar_sku:" + similar_sku + ", similarity:" + similarity);
		return sb.toString();
	}	
}
