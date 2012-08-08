package buzzard.utility;

public class KeyGenerator 
{
	
	private static int max_key = 0;
	
	public static int getKey()
	{
		
		return ++max_key;
	}

}
