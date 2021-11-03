using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Crystallics_Server
{
    class BusLight : TL
    {
        private const int RIGHT = 5;
        private const int STRAIGHT = 4;
        private bool isRight;

        public BusLight(int id, bool isRight)
            : base(id)
        {
            this.isRight = isRight;
        }


        protected override void SendMessage(int status)
        {

            JObject rootObject = new JObject();
            JArray lights = new JArray();
            JObject light = new JObject();
            light["id"] = id;
            if (status == Green)
            {
                if (id == 16 && isRight)
                {
                    light["status"] = RIGHT;
                }
                else
                {
                    light["status"] = STRAIGHT;
                }
            }
            else
            {
                light["status"] = status;
            }

            lights.Add(light);
            rootObject["stoplichten"] = lights;
            Program.server.send(rootObject);
            Console.WriteLine("sent messasge to client from TL");
        }
    }
}
