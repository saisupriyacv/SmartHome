package com.smarthome.network.model;

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
                "Controls":{
                  "Switch":  \"%s\",
                  "Alaram":  \"%s\"

                }
            }
         }
    }
*/



public class SmartHomeStatus {

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private State state;

    public class State {

        public Reported getReported() {
            return reported;
        }

        public void setReported(Reported reported) {
            this.reported = reported;
        }

        private Reported reported;

        public class Reported {

            public Reported.Doors getDoors() {
                return Doors;
            }

            public void setDoors(Reported.Doors doors) {
                Doors = doors;
            }

            public Reported.Controls getControls() {
                return Controls;
            }

            public void setControls(Reported.Controls controls) {
                Controls = controls;
            }

            private Doors Doors;

            private Controls Controls;

            private Temperature Temperature;

            public class Doors {
                public String getFrontDoor() {
                    return FrontDoor;
                }

                public void setFrontDoor(String frontDoor) {
                    FrontDoor = frontDoor;
                }

                public String getBackDoor() {
                    return BackDoor;
                }

                public void setBackDoor(String backDoor) {
                    BackDoor = backDoor;
                }

                public String getSideDoor() {
                    return SideDoor;
                }

                public void setSideDoor(String sideDoor) {
                    SideDoor = sideDoor;
                }

                private String FrontDoor;

                private String BackDoor;

                private String SideDoor;
            }

            public class Temperature {

                private String Temperature;

                public String getTemperature() {
                    return Temperature;
                }

                public void setTemperature(String temperature) {
                    Temperature = temperature;
                }
            }

            public class Controls {
                public String getSwitch() {
                    return Switch;
                }

                public void setSwitch(String aSwitch) {
                    Switch = aSwitch;
                }

                public String getAlaram() {
                    return Alaram;
                }

                public void setAlaram(String alaram) {
                    Alaram = alaram;
                }

                private String Switch;

                private String Alaram;
            }
        }
    }
}




