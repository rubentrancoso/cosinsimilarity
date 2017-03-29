package engine.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Similarity {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long name;
    private Long target;
    private double similarity;
    private double[] attrs = new double[10];
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getName() {
		return name;
	}
	public void setName(Long name) {
		this.name = name;
	}
	public Long getTarget() {
		return target;
	}
	public void setTarget(Long target) {
		this.target = target;
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
		sb.append("name:" + name + ", target:" + target + ", similarity:" + similarity);
		return sb.toString();
	}	
}
