package com.example.saisu.smarthome;

/**
 * Created by cvssa on 11/28/2016.
 * {"state":
 *  {
 *
 *
 *
 * }
 * }
 *  *
 *
 * {"state":{"reported":{"Controls":{"Alaram":"disarm","Switch":"off"},
 * "Doors":{"BackDoor":"Closed","SideDoor":"Open","FrontDoor":"Open"}}},
 * "metadata":{"reported":
 * {"Controls":
 * {"Alaram":
 * {"timestamp":1480440611},
 * "Switch":{"timestamp":1480440611}},
 * "Doors":{
 * "BackDoor":{"timestamp":1480440611},
 * "SideDoor":{"timestamp":1480440611},
 * "FrontDoor":{"timestamp":1480440611}}}
 * },
 * "version":3,
 * "timestamp":1480441089}
 *
 *
 *
 */
/*      {
        "state": {
            reported:{
                "Doors":{
                  "FrontDoor": \"%s\",
                  "BackDoor": \"%s\",
                  "SideDoor": \"%s\"
                },
                "Controls":{
                  "Switch":  \"%s\",
                  "Alaram":  \"%s\"

                }
            }
         }
    }

        "metadata":{
        "reported":{
        "Controls":
        {"Alaram":{"timestamp":1480440611},"Switch":{"timestamp":1480440611}},
        "Doors":{"BackDoor":{"timestamp":1480440611},"SideDoor":{"timestamp":1480440611},"FrontDoor":{"timestamp":1480440611}}}},
        "version":3,
        "timestamp":1480514000}
 */


public class SmartHomeStatus {
    public State state;

    SmartHomeStatus() {
        state = new State();
    }

    public class State {
         Reported reported;

        State() {
            reported = new Reported();
        }

        public class  Reported  {
            Door Doors;
            Control Controls;


            Reported() {
                Doors = new Door();
                Controls = new Control();
            }


        }
        public class Door{

            Door(){

            }

           String FrontDoor;
           String BackDoor;
            String SideDoor;
        }
        public class  Control{

            Control(){}

           String Switch;
           String Alaram;

       }
    }


    public Long version;
    public Long timestamp;
}




