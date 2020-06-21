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
    class PingServer
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
        Int32 port = 5600;
        String serverName;
        bool stop;
        TcpListener listener;
        public void StartServer()
        {
            if (!stop)
            {
                listener = new TcpListener(ipAddress, port);
            }
            listener.Start();
            Console.WriteLine("Ping server started - name " + serverName);
            while (!stop)
            {
                try
                {
                    TcpClient client = listener.AcceptTcpClient();
                    Console.WriteLine("Ping client connected");
                    var stream = client.GetStream();
                    var writer = new StreamWriter(stream);
                    Byte[] sendBytes = Encoding.ASCII.GetBytes(serverName+"\n");
                    stream.Write(sendBytes, 0, sendBytes.Length);
                    Console.WriteLine("Name sent");
                    client.Close();
                    Console.WriteLine("Closed ping client");
                }
                catch (SocketException e)
                {
                    if (stop)
                    {
                        Console.WriteLine("Ping server stopped");
                    }
                }
            }
        }

        public void StopServer()
        {
            stop = true;
            listener.Stop();
        }

        public void SetName(String n)
        {
            serverName = n;
        }
    }
}
