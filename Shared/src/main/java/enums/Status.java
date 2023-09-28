package enums;

public enum Status {
    ONLINE {
        @Override
        public String getText() {
            return "ONLINE";
        }

    }, OFFLINE {
        @Override
        public String getText() {
            return "OFFLINE";
        }
    };

    public abstract String getText();
}

