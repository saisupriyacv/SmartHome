package com.example.saisu.smarthome;

/**
 * Created by cvssa on 11/28/2016.
/*      {
        "state": {
            reported:{
                "Doors":{
                  "FrontDoor": \"%s\",
                  "BackDoor": \"%s\",
                  "SideDoor": \"%s\"
                },
                "Temperature": {
                    "LivingRoom": "81.0"
                },
                "Controls":{
                  "Switch":  \"%s\",
                  "Alaram":  \"%s\"

                }
            }
         }
    }
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
            Temp Temperature;


            Reported() {
                Doors = new Door();
                Controls = new Control();
                Temperature = new Temp();
            }
        }
        public class Door{

           public String getFrontDoor() {
                return FrontDoor;
            }
           public String getBackDoor() {
                return BackDoor;
            }
           public String getSideDoor() {
                return SideDoor;
            }


            public void setFrontDoor(String frontDoor) {
                FrontDoor = frontDoor;
            }

            public void setBackDoor(String backDoor) {
                BackDoor = backDoor;
            }

            public void setSideDoor(String sideDoor) {
                SideDoor = sideDoor;
            }

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


            public String getSwitch() {
                return Switch;
            }

            public String getAlaram() {
                return Alaram;
            }

            public void setSwitch(String aSwitch) {
                Switch = aSwitch;
            }

            public void setAlaram(String alaram) {
                Alaram = alaram;
            }
        }

        public class Temp{

            public String gettemperature() {
                return Temperature;
            }
            Temp(){
            }

            String Temperature;
        }
    }


    public Long version;
    public Long timestamp;
}




