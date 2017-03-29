package engine.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long name;
    private double[] attrs = new double[10];
    
    public Article() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public void setName(Long name) {
		this.name = name;
	}
	
	public Long getName() {
		return this.name;
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
		sb.append("name:" + name + ", attr:[");
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
}