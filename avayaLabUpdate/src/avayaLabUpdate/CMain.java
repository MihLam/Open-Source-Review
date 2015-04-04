package avayaLabUpdate;

public class CMain
{

	public static void main(String[] args)
	{
		new MyLogger("avayaLabUpdate");

		/**
		 * 0. one clickto:		MakeAvayaLabUpdate.java
		 * 
		 * 1. GetHTTP query		HTTPClient.java 
		 * 2. Parse HTTPfile	ParserHTMPFiles.java
		 * 3. get Version		Shell.java
		 * 3.1 parse output		ParserForMessaging.java
		 * 4. postHttp query	HTTPClient.java
		 * 
		 */
		MakeAvayaLabUpdate malu = new MakeAvayaLabUpdate();
		malu.makeIt();
		System.exit(0);
	}

}
