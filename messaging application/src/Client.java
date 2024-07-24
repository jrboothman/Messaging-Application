import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username)
    {
        try
        {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch (IOException e)
        {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage()
    {
        try
        {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while(socket.isConnected())
            {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e)
        {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String messageFromGroup;

                while(socket.isConnected())
                {
                    try
                    {
                        messageFromGroup = bufferedReader.readLine();
                        System.out.println(messageFromGroup);
                    }catch (IOException e)
                    {
                        closeAll(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try
        {
            if (bufferedReader != null)
            {
                bufferedReader.close();
            }
            if (bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);

        System .out.println("Enter the number of the option you wish to use");
        System.out.println("");
        System.out.println("1. Connect to the server");
        System.out.println("2. Exit");

        String choice = scanner.nextLine();
        int choiceint = 0;

        try
        {
            choiceint = Integer.parseInt(choice);
        }catch(NumberFormatException e)
        {
            e.printStackTrace();
        }

        if (choiceint == 1)
        {
            System.out.println("Enter the name you would like to use in the chat");
            String username = scanner.nextLine();
            Socket socket = new Socket("localhost", 12345);
            Client client = new Client(socket, username);
            client.listenForMessage();
            client.sendMessage();
        } else if (choiceint == 2)
        {
            System.exit(0);
        }
        else
        {
            System.out.println("Invalid Selection");
        }
    }
}
