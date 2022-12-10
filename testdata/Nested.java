

/**
 * 
 * @author Langming Xu
 *
 */
public class Nested {

	public void ifOne(int i, String str)
	{
		System.out.println("before it");
		if(i == 0) {
			System.out.println("if");
		}
		else if(i == 2) {
			if(x == 3);
			System.out.println("else if");
		}
		else {
			System.out.println("else");
		}
		
		while(str == "after if");
		
		System.out.println("after while");
	}
	
	public void ifTwo(int i)
	{
		System.out.println("before if");
		if(i == 0) {
			System.out.println("then");
			if(i == 1) {
				System.out.println("if-else in then");
			}
		}
		else {
			System.out.println("else");
			if(i == 2) {
				System.out.println("if-else in else");
			}
		}
		
		System.out.println("after if");
	}
	
	public void ifThree(int i)
	{
		System.out.println("before if");
		
		if(i == 0) {
			System.out.println("then");
			if(i == 1);
			else {
				System.out.println("if in then");
			}
		}
		else {
			System.out.println("else");
			if(i == 2) {
				System.out.println("if in else");
			}
		}
		
		System.out.println("after if");
	}
	
}
