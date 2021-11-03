using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace Crystallics_Server
{
    class DefaultLight : TL
    {
        public DefaultLight(int id) : base(id) { }
        // maakt een Json bericht aan van het ligt 
        protected override void SendMessage(int status)
        {
            JObject rootObject = new JObject();
            JArray lights = new JArray();
            JObject light = new JObject();
            light["id"] = id;
            light["status"] = status;
            lights.Add(light);
            rootObject["stoplichten"] = lights;
            Program.server.send(rootObject);
            Console.WriteLine("sent messasge to client from TL");
        }
    }
}
