using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Crystallics_Server
{
    class ClientSender
    {
        //thread aanmaken om dingen uit te voeren 
        private Thread workerThread;
        //private LightLogic Light;

        // de poel aan json berichten die moeten worden verzonden
        private List<JObject> pool;

        private readonly Object POOLLOCK = new Object();
        // om op de stream te kunnen schrijven.
        private StreamWriter sw;

        // consturctor for de sender naar de client, deze is om dingen naar de stream te versturen.
        public ClientSender(NetworkStream stream)
        {
            sw = new StreamWriter(stream);
            pool = new List<JObject>();
        }
        // constructore voor het toevoegen van een json object naar de poel.
        public ClientSender(JObject j)
        {
            List<JObject> Pool = new List<JObject>();
            Pool.Add(j);
            this.pool = Pool;
        }
        // start de klasse aan. 
        public void start()
        {
            if (workerThread != null)
            {
                workerThread.Interrupt();
            }
            workerThread = new Thread(pollAndSend);
            workerThread.Start();
        }
        // stop de klasse.
        public void stop()
        {
            if (workerThread != null)
            {
                workerThread.Interrupt();
            }
        }
        // 
        public void pollAndSend()
        {
            while (true)
            {
                JObject sendObject = popFromPool();
                try
                {
                    string message = JsonConvert.SerializeObject(sendObject);
                    sw.WriteLine(message);
                    sw.Flush();
                    Console.WriteLine("Sent message: " + message);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                }
            }
        }

        public void addToSendPool(JObject jObject)
        {
            lock (POOLLOCK)
            {
                pool.Add(jObject);
                Monitor.Pulse(POOLLOCK);
            }
        }

        public void addToSendPool(int id, int status)
        {
            JObject rootObject = new JObject();
            JArray lights = new JArray();
            JObject light = new JObject();
            light["id"] = id;
            light["status"] = status;
            lights.Add(light);
            rootObject["stoplichten"] = lights;

            lock (POOLLOCK)
            {
                pool.Add(rootObject);
                Monitor.Pulse(POOLLOCK);
            }
        }

        private JObject popFromPool()
        {
            lock (POOLLOCK)
            {
                if (pool.Count == 0)
                {
                    Monitor.Wait(POOLLOCK);
                }

                JObject o = pool[0];
                pool.RemoveAt(0);
                return o;
            }
        }
    }
}
