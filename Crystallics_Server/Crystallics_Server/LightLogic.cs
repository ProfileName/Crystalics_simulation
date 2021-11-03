using System;
using System.Collections.Generic;
using System.Threading;


namespace Crystallics_Server
{

    class PendingStatus
    {
        public int id { get; private set; }
        public bool status;
        public PendingStatus(int id, bool status) { this.id = id; this.status = status; }
    }

    class PendingBusStatus : PendingStatus
    {
        // set de weg/lijn waarde. 
        public int lijn { get; private set; }
        public PendingBusStatus(int id, int lijn, bool status) : base(id, status) { this.lijn = lijn; }
    }

    class LightLogic
    {
        private Thread thread;
        bool running;

        public Dictionary<int, TL> lights;
        public List<PendingStatus> pool;
        private readonly object poolLock = new object();

        private bool isBusId(int id)
        {
            return id == 15 || id == 16;
        }
        public LightLogic()
        {


            lights = new Dictionary<int, TL>();
            pool = new List<PendingStatus>();

            for (int i = 0; i < 35; ++i)
            {
                if (isBusId(i))
                {
                    lights.Add(i, new BusLight(i, false));
                }
                else
                {
                    lights.Add(i, new DefaultLight(i));
                }
            }
            lights.Add(35, new BusLight(16, true)); //bus 15 rechtsaf krijgt id 35

            lights[23].minGreenTime =
                lights[24].minGreenTime = lights[25].minGreenTime = lights[28].minGreenTime = lights[29].minGreenTime =
                lights[33].minGreenTime = lights[34].minGreenTime = lights[27].minGreenTime = lights[32].minGreenTime = lights[30].minGreenTime
                = lights[26].minGreenTime = lights[31].minGreenTime = 8;

            lights[17].minGreenTime = lights[19].minGreenTime = lights[20].minGreenTime = lights[21].minGreenTime
                = lights[22].minGreenTime = 8;

            lights[15].minGreenTime = lights[16].minGreenTime = 10;

        }
        private List<int> getConflictingLights(int id)
        {
            switch (id)
            {
                case 0:
                    return new List<int>() { 4, 8, 13 };
                case 1:
                    return new List<int>() { 5, 6, 9, 10, 11, 12, 13, 16, 25, 26, 17 };
                case 2:
                    return new List<int>() { 5, 6, 7, 8, 12, 13, 33, 31, 25, 26, 31, 22, 17 };
                case 3:
                    return new List<int>() { 5, 6, 7, 8, 12, 13, 31, 25, 26, 22, 17 };
                case 4:
                    return new List<int>() { 8, 13, 25, 26, 15, 28, 0, 17 };

                case 5:
                    return new List<int>() { 1, 2, 3, 8, 9, 10, 13, 14, 20, 29, 30, 23, 24, 15 };
                case 6:
                    return new List<int>() { 1, 2, 3, 8, 9, 10, 11, 16, 20, 29, 30, 15 };

                case 7:
                    return new List<int>() { 2, 3, 15, 32, 31, 22, 21 };

                case 8:
                    return new List<int>() { 2, 3, 4, 5, 6, 12, 13, 28, 19, 27, 15, 0 };

                case 9:
                    return new List<int>() { 5, 6, 12, 13, 1, 23, 24, 17, 34 };

                case 10:
                    return new List<int>() { 5, 6, 12, 13, 1, 23, 24, 17, 14, 34 };

                case 11:
                    return new List<int>() { 16, 21, 33, 6, 1, 34 };


                case 12:
                    return new List<int> { 1, 2, 3, 6, 7, 8, 9, 22, 31, 32, 21, 10, 16 };

                case 13:
                    return new List<int> { 1, 2, 3, 4, 5, 8, 16, 10, 8, 9, 19, 27, 28, 0 };

                case 14:
                    return new List<int> { 10, 9, 23, 24, 17, 16 };
                case 15: //bus 15 voor lijn 70
                    return new List<int> { 5, 6, 7, 4, 13, 8, 32, 31, 21, 22 };
                case 16: //bus 16 rechtdoor voor lijn 97
                    return new List<int> { 22, 21, 33, 34, 11, 6, 1, 23, 17, 24, 13, 14, 12 };
                case 35: //bus 16 rechtsaf voor lijn 71
                    return new List<int> { 22, 21, 33, 34, 11, 6, 1 };
                case 17:
                    return new List<int> { 10, 9, 16, 5, 14, 1, 2, 3, 4, 15 };
                case 18:

                    break;
                case 19:
                    return new List<int> { 5, 6, 7, 4, 13, 8 };

                case 20:
                    return new List<int> { 5, 6, 7, 4, 13, 8 };
                case 21:
                    return new List<int> { 3, 2, 7, 12, 8, 9, 10, 11, 16, 15 };
                case 22:
                    return new List<int> { 3, 2, 7, 12, 8, 9, 10, 11, 16, 15 };
                case 23:
                    return new List<int> { 9, 10, 14, 5 };
                case 24:
                    return new List<int> { 9, 10, 14, 5 };
                case 25:
                    return new List<int> { 1, 2, 3, 4, 15 };
                case 26:
                    return new List<int> { 1, 2, 3, 4, 15 };
                case 27:
                    return new List<int> { 4, 13, 8 };
                case 28:
                    return new List<int> { 4, 13, 8 };
                case 29:
                    return new List<int> { 5, 6, 7 };
                case 30:
                    return new List<int> { 5, 6, 7 };
                case 31:
                    return new List<int> { 2, 3, 15, 12, 7 };
                case 32:
                    return new List<int> { 2, 3, 15, 12, 7 };
                case 33:
                    return new List<int> { 8, 9, 10, 11, 16 };
                case 34:
                    return new List<int> { 8, 9, 10, 11, 16 };

                default:
                    break;


            }
            return new List<int> { };
        }

        public void addToBusPool(int id, int lijn, bool status)
        {
            lock (poolLock)
            {
                pool.Add(new PendingBusStatus(id, lijn, status));
            }
        }

        public void addToPool(int id, bool status)
        {
            lock (poolLock)
            {
                foreach (PendingStatus pending in pool)
                {
                    if (pending.id == id)
                    {
                        pending.status = status;
                    }
                }

                pool.Add(new PendingStatus(id, status));
            }
        }


        public void stop()
        {
            if (thread != null)
            {
                running = false;
                thread.Interrupt();
            }
        }
        public void start()
        {
            stop();
            thread = new Thread(update);
            thread.Start();
            for (int i = 0; i < lights.Count; ++i)
            {
                lights[i].Start();
            }
        }

        public void update()
        {
            running = true;
            while (running)
            {
                try
                {
                    lock (poolLock)
                    {
                        if (pool.Count > 0)
                        {
                            List<PendingStatus> toRemove = new List<PendingStatus>();

                            //first check busses, priority!
                            foreach (PendingStatus newStatus in pool)
                            {
                                if (newStatus is PendingBusStatus)
                                {
                                    if (dispatchMessage(newStatus))
                                    {
                                        toRemove.Add(newStatus);
                                    }
                                }

                            }
                            foreach (PendingStatus s in toRemove)
                            {
                                pool.Remove(s);
                            }
                            //end

                            //check all other vehicles
                            foreach (PendingStatus newStatus in pool)
                            {
                                if (dispatchMessage(newStatus))
                                {
                                    toRemove.Add(newStatus);
                                }
                            }
                            foreach (PendingStatus s in toRemove)
                            {
                                pool.Remove(s);
                            }
                        }
                    }
                    foreach (KeyValuePair<int, TL> pair in lights)
                    {
                        TL light = pair.Value;
                        if (light.status == TL.Red && light.hasVehicles)
                        {
                            List<int> conflict = getConflictingLights(light.id);
                            bool isConflicting = false;
                            foreach (int i in conflict)
                            {
                                TL conflictingLight = lights[i];
                                if (conflictingLight.status != TL.Red)
                                {
                                    isConflicting = true;
                                }
                            }
                            if (!isConflicting)
                            {
                                light.SetGreen();
                            }
                        }
                    }
                    Thread.Sleep(10);
                }
                catch (ThreadInterruptedException) { running = false; }
            }
        }


        private bool dispatchMessage(PendingStatus newStatus)
        {

            int id = newStatus.id;
            bool newState = newStatus.status;
            Console.WriteLine("id: " + id);
            if (!lights.ContainsKey(id)) { return true; }

            TL light = null;

            if (id == 16)
            { //if bus
                int lijn = ((PendingBusStatus)newStatus).lijn;
                switch (lijn)
                {

                    case 97: //rechtdoor
                             //id stay 16
                        break;
                    case 71: //rechtsaf
                        id = 35;
                        break;
                }
            }
            light = lights[id];

            if (!newState)
            {
                light.hasVehicles = false;
                return true;
            }


            List<int> conflict = getConflictingLights(id);
            bool isConflicting = false;
            foreach (int i in conflict)
            {
                TL conflictingLight = lights[i];
                if (conflictingLight.status != TL.Red)
                {
                    isConflicting = true;
                }
            }
            if (!isConflicting)
            {
                light.hasVehicles = true;
                light.SetGreen();
                return true;
            }


            return false;
        }
    }
}
