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
    class ClientReceiver
    {
        private StreamReader reader;
        private Thread workerThread;
        private Server server;

        public ClientReceiver(Server server, NetworkStream stream)
        {
            reader = new StreamReader(stream, Encoding.UTF8);
            this.server = server;
        }


        public void start()
        {
            if (workerThread != null)
            {
                workerThread.Interrupt();
            }
            workerThread = new Thread(readFromStream);
            workerThread.Start();
        }

        public void stop()
        {
            if (workerThread != null)
            {
                workerThread.Interrupt();
            }
        }

        public void readFromStream()
        {
            Console.WriteLine("reading");

            try
            {
                while (true)
                {
                    string line;
                    while ((line = reader.ReadLine()) != null)
                    {
                        Console.WriteLine(line);
                        JObject j = JObject.Parse(line);

                        JToken roads = j["banen"]; //token contains array of roads
                        if (roads == null)
                        {
                            roads = j["busbanen"];
                            foreach (JToken token in roads)
                            {
                                int id = (int)token["id"];
                                bool bezet = (bool)token["bezet"];
                                int lijn = (int)token["eerstvolgendelijn"];

                                Program.lightLogic.addToBusPool(id, lijn, bezet);
                                Console.WriteLine(id.ToString());
                            }
                        }
                        else
                        {
                            foreach (JToken token in roads)
                            {
                                int id = (int)token["id"];
                                bool bezet = (bool)token["bezet"];
                                Program.lightLogic.addToPool(id, bezet);
                                Console.WriteLine(id.ToString());
                            }
                        }
                        // Hier komt de code van het verkeer
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                server.lostConnection();

            }

        }
    }
}
