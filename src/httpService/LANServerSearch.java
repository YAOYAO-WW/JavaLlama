package httpService;
import java.net.*;


//TODO: Implement the LANServerSearch class, which provides a method to search the local network for the server's IP address.
public class LANServerSearch {
    public static String getServerAddress() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
