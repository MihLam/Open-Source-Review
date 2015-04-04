package avayaLabUpdate;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Shell implements Sui
{
	private static final int SSH_PORT = 22;
	private static final String user = "";
	private static final String pass = "";
	private static final String swvesionCommand = "swversion | grep \"Messaging:\"";

	public void getSwversion(String host, String server) throws Exception
	{
		FileOutputStream _fos = new FileOutputStream(pathToTmpFile + server + ".txt");

		Session session = (new JSch()).getSession(user, host, SSH_PORT);
		session.setPassword(pass);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
		Channel channel = session.openChannel("shell");
		OutputStream inputstream_for_the_channel = channel.getOutputStream();
		PrintStream commander = new PrintStream(inputstream_for_the_channel, true);

		channel.setOutputStream(_fos);
		channel.connect();
		commander.println("");
		commander.println(swvesionCommand);
		commander.println("exit");
		commander.close();

		while (channel.isConnected())
			;

		MyLogger.addInfoLog("get Version succeeded ip:" + host);
		_fos.close();
		session.disconnect();

	}
}
