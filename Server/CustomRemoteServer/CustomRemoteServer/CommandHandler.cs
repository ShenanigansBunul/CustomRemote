using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using static Win32;

namespace CustomRemoteServer
{
    public sealed class CommandHandler
    {
        private static CommandHandler instance = null;
        private Dictionary<char, KeyCode> lowercaseKeyCodeDict;
        private Dictionary<char, KeyCode> uppercaseKeyCodeDict;
        private CommandHandler() {
            lowercaseKeyCodeDict = new Dictionary<char, KeyCode>(){
                { '`', KeyCode.L1 },
                { '1', KeyCode.L2 },
                { '2', KeyCode.L3 },
                { '3', KeyCode.L4 },
                { '4', KeyCode.L5 },
                { '5', KeyCode.L6 },
                { '6', KeyCode.L7 },
                { '7', KeyCode.L8 },
                { '8', KeyCode.L9 },
                { '9', KeyCode.L10 },
                { '0', KeyCode.L11 },
                { '-', KeyCode.L12 },
                { '=', KeyCode.L13 },
                { '[', KeyCode.L14 },
                { ']', KeyCode.L15 },
                { '\\', KeyCode.L16 },
                { ';', KeyCode.L17 },
                { '\'', KeyCode.L18 },
                { ',', KeyCode.L19 },
                { '.', KeyCode.L20 },
                { '/', KeyCode.L21 },
                { 'a', KeyCode.L22 },
                { 'b', KeyCode.L23 },
                { 'c', KeyCode.L24 },
                { 'd', KeyCode.L25 },
                { 'e', KeyCode.L26 },
                { 'f', KeyCode.L27 },
                { 'g', KeyCode.L28 },
                { 'h', KeyCode.L29 },
                { 'i', KeyCode.L30 },
                { 'j', KeyCode.L31 },
                { 'k', KeyCode.L32 },
                { 'l', KeyCode.L33 },
                { 'm', KeyCode.L34 },
                { 'n', KeyCode.L35 },
                { 'o', KeyCode.L36 },
                { 'p', KeyCode.L37 },
                { 'q', KeyCode.L38 },
                { 'r', KeyCode.L39 },
                { 's', KeyCode.L40 },
                { 't', KeyCode.L41 },
                { 'u', KeyCode.L42 },
                { 'v', KeyCode.L43 },
                { 'w', KeyCode.L44 },
                { 'x', KeyCode.L45 },
                { 'y', KeyCode.L46 },
                { 'z', KeyCode.L47 },
                { ' ', KeyCode.Space }
            };

            uppercaseKeyCodeDict = new Dictionary<char, KeyCode>()
            {
                { '~', KeyCode.L1 },
                { '!', KeyCode.L2 },
                { '@', KeyCode.L3 },
                { '#', KeyCode.L4 },
                { '$', KeyCode.L5 },
                { '%', KeyCode.L6 },
                { '^', KeyCode.L7 },
                { '&', KeyCode.L8 },
                { '*', KeyCode.L9 },
                { '(', KeyCode.L10 },
                { ')', KeyCode.L11 },
                { '_', KeyCode.L12 },
                { '+', KeyCode.L13 },
                { '{', KeyCode.L14 },
                { '}', KeyCode.L15 },
                { '|', KeyCode.L16 },
                { ':', KeyCode.L17 },
                { '"', KeyCode.L18 },
                { '<', KeyCode.L19 },
                { '>', KeyCode.L20 },
                { '?', KeyCode.L21 },
                { 'A', KeyCode.L22 },
                { 'B', KeyCode.L23 },
                { 'C', KeyCode.L24 },
                { 'D', KeyCode.L25 },
                { 'E', KeyCode.L26 },
                { 'F', KeyCode.L27 },
                { 'G', KeyCode.L28 },
                { 'H', KeyCode.L29 },
                { 'I', KeyCode.L30 },
                { 'J', KeyCode.L31 },
                { 'K', KeyCode.L32 },
                { 'L', KeyCode.L33 },
                { 'M', KeyCode.L34 },
                { 'N', KeyCode.L35 },
                { 'O', KeyCode.L36 },
                { 'P', KeyCode.L37 },
                { 'Q', KeyCode.L38 },
                { 'R', KeyCode.L39 },
                { 'S', KeyCode.L40 },
                { 'T', KeyCode.L41 },
                { 'U', KeyCode.L42 },
                { 'V', KeyCode.L43 },
                { 'W', KeyCode.L44 },
                { 'X', KeyCode.L45 },
                { 'Y', KeyCode.L46 },
                { 'Z', KeyCode.L47 },
            };
        }
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
                    else if (commandName == "KEY")
                        PressKey(commandParams[0]);
                    else if (commandName == "WRT")
                    {
                        Thread thread = new Thread(() => WriteText(commandParams[0]));
                        thread.Start();
                    }
                        
                }
            }
        }

        private KeyCode CharToKeyCode(Char c)
        {
            if (lowercaseKeyCodeDict.ContainsKey(c))
            {
                return lowercaseKeyCodeDict[c];
            }
            else if (uppercaseKeyCodeDict.ContainsKey(c))
            {
                return uppercaseKeyCodeDict[c];
            }

            return KeyCode.None;
        }

        private void WriteText(string v)
        {
            foreach (Char c in v)
            {
                if (uppercaseKeyCodeDict.ContainsKey(c))
                    SendKeyDown(KeyCode.Shift);
                KeyCode key = CharToKeyCode(c);
                SendKeyPress(key);
                if (uppercaseKeyCodeDict.ContainsKey(c))
                    SendKeyUp(KeyCode.Shift);
            }
        }

        private void PressKey(string v)
        {
            int val = int.Parse(v);
            SendKeyPress((KeyCode)val);
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
            Win32.SetCursorPos(p.x, p.y);
        }

        public void LeftClick()
        {
            mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, new UIntPtr());
            Thread.Sleep(50);
            mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, new UIntPtr());
        }
        public void RightClick()
        {
            mouse_event(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, 0, 0, 0, new UIntPtr());
        }

        public static void SendKeyPress(KeyCode keyCode)
        {
            SendKeyDown(keyCode);
            SendKeyUp(keyCode);
        }

        public static void SendKeyDown(KeyCode keyCode)
        {
            INPUT input = new INPUT
            {
                Type = 1
            };
            input.Data.Keyboard = new KEYBDINPUT();
            input.Data.Keyboard.Vk = (ushort)keyCode;
            input.Data.Keyboard.Scan = 0;
            input.Data.Keyboard.Flags = 0;
            input.Data.Keyboard.Time = 0;
            input.Data.Keyboard.ExtraInfo = IntPtr.Zero;
            INPUT[] inputs = new INPUT[] { input };
            if (SendInput(1, inputs, Marshal.SizeOf(typeof(INPUT))) == 0)
            {
                throw new Exception();
            }
        }

        public static void SendKeyUp(KeyCode keyCode)
        {
            INPUT input = new INPUT
            {
                Type = 1
            };
            input.Data.Keyboard = new KEYBDINPUT();
            input.Data.Keyboard.Vk = (ushort)keyCode;
            input.Data.Keyboard.Scan = 0;
            input.Data.Keyboard.Flags = 2;
            input.Data.Keyboard.Time = 0;
            input.Data.Keyboard.ExtraInfo = IntPtr.Zero;
            INPUT[] inputs = new INPUT[] { input };
            if (SendInput(1, inputs, Marshal.SizeOf(typeof(INPUT))) == 0)
                throw new Exception();

        }
    }
}
