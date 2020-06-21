using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace CustomRemoteServer
{
    class CommandServer
    {
        private static IPAddress LocalIPAddress()
        {
            if (!System.Net.NetworkInformation.NetworkInterface.GetIsNetworkAvailable())
            {
                return null;
            }

            IPHostEntry host = Dns.GetHostEntry(Dns.GetHostName());

            return host
                .AddressList
                .FirstOrDefault(ip => ip.AddressFamily == AddressFamily.InterNetwork);
        }

        IPAddress ipAddress = LocalIPAddress(); //IPAddress.Parse("192.168.1.9");
        Int32 port = 5800;
        TcpListener listener;
        bool stop;
        public CommandServer()
        {
        }

        public void StartServer()
        {
            if (!stop)
            {
                listener = new TcpListener(ipAddress, port);
            }
            
            listener.Start();
            Console.WriteLine("Command server started");
            stop = false;
            CommandHandler commandHandler = CommandHandler.GetInstance();

            Byte[] bytes = new Byte[1024];
            String data;

            while (!stop)
            {
                try
                {
                    TcpClient client = listener.AcceptTcpClient();
                    Console.WriteLine("Command client");
                    var stream = client.GetStream();
                    var reader = new StreamReader(stream);
                    StreamWriter writer = new StreamWriter(stream);
                    writer.NewLine = "\r\n";
                    writer.AutoFlush = true;
                    bool clientStop = false;
                    while (!stop && !clientStop)
                    {
                        byte[] serverData = new byte[client.ReceiveBufferSize];
                        int length = stream.Read(serverData, 0, serverData.Length);
                        if (length == 0)
                        {
                            clientStop = true;
                            writer.Close();
                            reader.Close();
                            stream.Close();
                            client.Close();
                        }
                        else
                        {
                            string received = Encoding.ASCII.GetString(serverData, 0, length);
                            if (received != "")
                            {
                                Console.WriteLine("this is a received:");
                                Console.WriteLine(received);
                                commandHandler.HandleCommand(received);
                            }
                        }
                    }
                }
                catch(SocketException e)
                {
                    if (stop)
                    {
                        Console.WriteLine("Command server stopped");
                    }
                }
                data = null;
                int i;
            }
        }

        public void StopServer()
        {
            stop = true;
            listener.Stop();
        }
    }
}
