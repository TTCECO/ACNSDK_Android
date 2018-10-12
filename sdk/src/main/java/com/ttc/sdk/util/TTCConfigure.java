package com.ttc.sdk.util;

public class TTCConfigure {

    private boolean logEnabled;

    private boolean serverEnabled;

    private TTCConfigure(boolean logEnabled, boolean serverEnabled) {
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

        public TTCConfigure build() {
            return new TTCConfigure(logEnabled, serverEnabled);
        }
    }


}
