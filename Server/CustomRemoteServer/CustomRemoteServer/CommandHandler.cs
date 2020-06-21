using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using static Win32;

namespace CustomRemoteServer
{
    public sealed class CommandHandler
    {
        private static CommandHandler instance = null;
        private CommandHandler() { }
        public static CommandHandler GetInstance()
        {
            if (instance == null)
                instance = new CommandHandler();
            return instance;
        }
        public void HandleCommand(String com) {
            String[] comms = com.Split('\n');
            foreach (String s in comms)
            {
                var cs = s.Split(':');
                if (cs.Length > 0)
                {
                    String commandName = cs[0];
                    String[] commandParams = new String[2];
                    if (cs.Length > 1)
                    {
                        commandParams = cs[1].Split(',');
                    }
                    if (commandName == "MD")
                        MoveMouse(commandParams[0], commandParams[1]);
                    else if (commandName == "MLC")
                        new Thread(LeftClick).Start();
                    else if (commandName == "MRC")
                        RightClick();
                }
            }
        }
        public void MoveMouse(String delta_x, String delta_y)
        {
            POINT current;
            GetCursorPos(out current);
            Win32.POINT p = new Win32.POINT();
            var f_x = double.Parse(delta_x);
            var i_x = Convert.ToInt32(f_x);
            var f_y = double.Parse(delta_y);
            var i_y = Convert.ToInt32(f_y);
            p.x = Convert.ToInt16(current.x + i_x);
            p.y = Convert.ToInt16(current.y + i_y);
            //Win32.ClientToScreen(this.Handle, ref p);
            Win32.SetCursorPos(p.x, p.y);
        }

        public void LeftClick()
        {
            mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, new UIntPtr());
            Thread.Sleep(100);
            mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, new UIntPtr());
        }
        public void RightClick()
        {
            mouse_event(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, 0, 0, 0, new UIntPtr());
        }
    }
}
