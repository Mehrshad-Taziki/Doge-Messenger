package enums;

public enum SeenStatus {
    NOTSENT {
        @Override
        public int getStage() {
            return 0;
        }

        @Override
        public String toString() {
            return "0";
        }
    },
    SENT {
        @Override
        public int getStage() {
            return 1;
        }

        @Override
        public String toString() {
            return "1";
        }
    },
    ONLINENOTSEEN {
        @Override
        public int getStage() {
            return 2;
        }

        @Override
        public String toString() {
            return "2";
        }
    },
    SEEN {
        @Override
        public int getStage() {
            return 3;
        }

        @Override
        public String toString() {
            return "3";
        }
    };

    public static SeenStatus getStatus(int status) {
        return switch (status) {
            case 3 -> SEEN;
            case 2 -> ONLINENOTSEEN;
            case 1 -> SENT;
            default -> NOTSENT;
        };
    }

    public abstract int getStage();
}
