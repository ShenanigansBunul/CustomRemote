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
        Thread commandServerThread;
        CommandServer commandServer = new CommandServer();
        public Form1()
        {
            InitializeComponent();
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

        public void MoveMouse()
        {
            Win32.POINT p = new Win32.POINT();
            p.x = Convert.ToInt16(33);
            p.y = Convert.ToInt16(333);
            //Win32.ClientToScreen(this.Handle, ref p);
            Win32.SetCursorPos(p.x, p.y);
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
        }

        private void UpdateServerButton()
        {
            if(commandServerStatus == 0)
            {
                button1.Text = "Start Server";
                button1.Enabled = true;
            }
            else if(commandServerStatus == 1)
            {
                button1.Text = "Starting Server...";
                button1.Enabled = false;
            }
            else if(commandServerStatus == 2 || commandServerStatus == 3)
            {
                button1.Text = "Stop Server";
                button1.Enabled = true;
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

                commandServerStatus = 2;
                UpdateServerButton();
            }
            else if(commandServerStatus == 2 || commandServerStatus == 3)
            {
                commandServer.StopServer();
                commandServerStatus = 0;
                UpdateServerButton();
            }
        }
    }
}
