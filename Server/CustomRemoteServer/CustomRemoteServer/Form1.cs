using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;

namespace CustomRemoteServer
{
    public partial class Form1 : Form
    {
        int commandServerStatus = 0; // stopped, starting, running non connected, running connected
        int screenServerStatus = 0; // stopped, starting, running non connected, running connected??
        string clientName = "";
        string serverName = Environment.MachineName + "-SERVER";
        Thread commandServerThread, pingServerThread;
        CommandServer commandServer = new CommandServer();
        PingServer pingServer = new PingServer();
        public Form1()
        {
            InitializeComponent();
            CommandHandler ch = CommandHandler.GetInstance();
            textBox1.Text = serverName;
        }

        private void Form1_Resize(object sender, EventArgs e)
        {
            Console.WriteLine("Test");
            if (this.WindowState == FormWindowState.Minimized)
            {
                Hide();
                notifyIcon1.Visible = true;
            }
        }


        private void RestoreWindow(object sender, MouseEventArgs e)
        {
            Show();
            this.WindowState = FormWindowState.Normal;
            notifyIcon1.Visible = false;
        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void ChangeServerName(object sender, EventArgs e)
        {
            serverName = textBox1.Text;
            pingServer.SetName(textBox1.Text);
        }

        private void UpdateServerButton()
        {
            if(commandServerStatus == 0)
            {
                button1.Text = "Start Server";
                button1.Enabled = true;
                textBox1.Enabled = true;
            }
            else if(commandServerStatus == 1)
            {
                button1.Text = "Starting Server...";
                button1.Enabled = false;
                textBox1.Enabled = false;
            }
            else if(commandServerStatus == 2 || commandServerStatus == 3)
            {
                button1.Text = "Stop Server";
                button1.Enabled = true;
                textBox1.Enabled = false;
            }
        }

        private void StartServer(object sender, EventArgs e)
        {
            if (commandServerStatus == 0)
            {
                commandServerStatus = 1;
                UpdateServerButton();
                commandServerThread = new Thread(commandServer.StartServer);
                commandServerThread.Start();
                pingServerThread = new Thread(pingServer.StartServer);
                pingServerThread.Start();


                commandServerStatus = 2;
                UpdateServerButton();
            }
            else if(commandServerStatus == 2 || commandServerStatus == 3)
            {
                commandServer.StopServer();
                pingServer.StopServer();
                commandServerStatus = 0;
                UpdateServerButton();
            }
        }
    }
}
