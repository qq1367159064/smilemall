package cn.smilemall.common;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-24
 */
public class Test {
	
	
	public static void main(String[] args) {
		System.out.println(test(20, 458.58, 0.0005) * 2);
	}
	
	public static double test(Integer date, double price, double interest) {
		double totalPrice = 0;
		int fig = 1;
		while (true) {
			if(fig > date) {
				break;
			}
			totalPrice += price * fig * interest;
			fig ++;
		}
		return totalPrice;
	}
}
