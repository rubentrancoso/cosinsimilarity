package engine.entities;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;


public class Similarity {

    public final long sku;
    public final long similar_sku;
    public final double similarity;
    
    public Similarity(long sku, long similar_sku, double similarity) {
    	this.sku = sku;
    	this.similar_sku = similar_sku;
    	this.similarity = similarity;
    }
    
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("sku:" + sku + ", similar_sku:" + similar_sku + ", similarity:" + similarity);
		return sb.toString();
	}	
	
	public static final Attribute<Similarity, Long> ID = new SimpleAttribute<Similarity, Long>("sku") {
        public Long getValue(Similarity similarity, QueryOptions queryOptions) { return similarity.sku; }
    };

	public static final Attribute<Similarity, Long> SIMILAR_ID = new SimpleAttribute<Similarity, Long>("similar_sku") {
        public Long getValue(Similarity similarity, QueryOptions queryOptions) { return similarity.similar_sku; }
    };

}
