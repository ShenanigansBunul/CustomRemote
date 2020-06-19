using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace CustomRemoteServer
{
    class PingServer
    {
        IPAddress ipAddress = IPAddress.Parse("192.168.1.9");
        Int32 port = 5600;
        public void StartServer()
        {
            TcpListener listener = new TcpListener(ipAddress, port);
            listener.Start();
            while (true)
            {
                TcpClient client = listener.AcceptTcpClient();
                client.Close();
            }
        }
    }
}
