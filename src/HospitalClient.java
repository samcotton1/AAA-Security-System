import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedOutputStream;
import java.io.OutputStream;

public class HospitalClient {
    private static SSLSocket socket;

    public static void main(String[] args) throws Exception 
    {
        HospitalServer.init();
        GUIBuilder builder=new GUIBuilder();
        builder.loadWelcomeScreen();
        boolean successful=createSocket();
        if(successful)
        {
            builder.loadLogin();
        }
        // send("test");
    }

    // private static void startGUI() 
    // {
    //     GUIBuilder gui = new GUIBuilder();
    //     gui.loadWelcomeScreen();
    //     gui.loadLogin();
    // }

    private static boolean createSocket() throws Exception 
    {
        try
        {
            socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("localhost", 8080);
            socket.setEnabledProtocols(HospitalServer.protocols);
            socket.setEnabledCipherSuites(HospitalServer.cipher_suites);
            socket.startHandshake(); //explicitly begin handshake between server and client 
            System.out.println("Handshake successful");
            return true; //indicates the handshake between client and server was successful
        }
        catch(Exception e)
        {
            System.out.println("Error "+e);
        }
        return false; //handshake unsuccessful
    }

    private static void send(String message) throws Exception {
        OutputStream os = new BufferedOutputStream(socket.getOutputStream());
        os.write(message.getBytes());
        os.flush();
    }
}
