package avayaLabUpdate;

// S.u.i. = Some useful information
public interface Sui
{
	String pathToTmpFile = System.getProperty("user.dir") + "\\tmp";
	String pathToHttpGetFile = System.getProperty("user.dir") + "\\getHttp.html";
	String IPV4_PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
}
