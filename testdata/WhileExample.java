package cn.edu.whu.cstar.testingcourse.cfgparser;


public class WhileExample {

	public void whileOne()
	{
		int x = 0;
		while(x > 3)
			while(x < 8)
				x ++;
		x --;
	}
	
	
	public void whileIfOne()
	{
		int x = 0;
		if(x > 5)
		{
			while(x > 3)
			{
				if(x < 10)
					while(x < 8);
				x ++;
			}
			
		}
	
		x --;
	}
	
	public void switchCaseOne()
	{
		int x = 3;
		switch(x)
		{
		case 1:
			x ++;
			break;
		case 2:
			x --;
			break;
		default:
			
		}
		return;
	}
	
	public int tryCatchOne()
	{
		int x = 0;
		while(x ++ < 10)
		{
			try {
				x ++;
				
			} catch(NumberFormatException ex)
			{
				x --;
				
			} catch(NullPointerException ex)
			{
				x += 2;
				
			} finally 
			{
				x -= 2;
			}
		}
		
		return x;
	}
	
	public void breakContinueOne()
	{
		int x = 3;
		while(x > 0)
			if(x > 0)
			{	x ++; break;} 
			else if(x < 0)
			{	x --; continue;}
		
	}
}
