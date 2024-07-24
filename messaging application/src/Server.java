import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private ServerSocket serversocket;

    public Server(ServerSocket serversocket)
    {
        this.serversocket = serversocket;
    }

    public void startServer()
    {
        try
        {
            while (!serversocket.isClosed())
            {
                Socket socket = serversocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e)
        {

        }
    }

    public void closeServerSocket()
    {
        try
        {

            if (serversocket != null)
            {
                serversocket.close();
            }

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(12345);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}