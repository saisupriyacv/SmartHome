package com.smarthome.network.model;

/**
 * Created by cvssa on 11/28/2016.
 */

public class SmartHomeControl {
    private State state;

    private Long timestamp;

    private Long version;

    public class State {
        private Desired desired;

        private Delta delta;

        public Desired getDesired() {
            return desired;
        }

        public void setDesired(Desired desired) {
            this.desired = desired;
        }

        public Delta getDelta() {
            return delta;
        }

        public void setDelta(Delta delta) {
            this.delta = delta;
        }

        public class Desired {

            private Boolean enabled;
            private Integer setPoint;

            public Boolean getEnabled() {
                return enabled;
            }

            public void setEnabled(Boolean enabled) {
                this.enabled = enabled;
            }

            public Integer getSetPoint() {
                return setPoint;
            }

            public void setSetPoint(Integer setPoint) {
                this.setPoint = setPoint;
            }
        }

        public class Delta {

            private Integer setPoint;
            private Boolean enabled;

            public Integer getSetPoint() {
                return setPoint;
            }

            public void setSetPoint(Integer setPoint) {
                this.setPoint = setPoint;
            }

            public Boolean getEnabled() {
                return enabled;
            }

            public void setEnabled(Boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
