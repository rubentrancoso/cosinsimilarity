package engine.facts;

public class Weight {

	static private int[] attrs = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

	public static void config(String dotproductWeigths) {
		String[] strAttrs = dotproductWeigths.split(",");
		for (int i = 0; i < strAttrs.length; i++) {
			attrs[i] = Integer.parseInt(strAttrs[i]);
		}		
	}

	public static int get(int index) {
		return attrs[index];
	}
}
