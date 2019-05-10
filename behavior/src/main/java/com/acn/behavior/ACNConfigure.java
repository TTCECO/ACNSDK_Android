package com.acn.behavior;

/**
 * configure log and server switcher
 */
public class ACNConfigure {
    private boolean logEnabled;
    private boolean serverEnabled;

    private ACNConfigure(boolean logEnabled, boolean serverEnabled) {
        this.logEnabled = logEnabled;
        this.serverEnabled = serverEnabled;
    }

    public boolean logEnabled() {
        return logEnabled;
    }

    public boolean serverEnabled() {
        return serverEnabled;
    }

    public static class Builder {
        private boolean logEnabled;
        private boolean serverEnabled = true;

        public Builder logEnabled(boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        public Builder serverEnabled(boolean serverEnabled) {
            this.serverEnabled = serverEnabled;
            return this;
        }

        public ACNConfigure build() {
            return new ACNConfigure(logEnabled, serverEnabled);
        }
    }
}
