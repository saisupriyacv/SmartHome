package com.example.saisu.smarthome;

/**
 * Created by cvssa on 11/28/2016.
 */

public class SmartHomeControl {
    public State state;

    SmartHomeControl() {
        state = new State();
    }

    public class State {
        Desired desired;
        Delta delta;

        State() {
            desired = new Desired();
            delta = new Delta();
        }

        public class Desired {
            Desired() {
            }

            public Integer setPoint;
            public Boolean enabled;
        }

        public class Delta {
            Delta() {
            }

            public Integer setPoint;
            public Boolean enabled;
        }
    }

    public Long version;
    public Long timestamp;
}
