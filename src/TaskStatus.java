public enum TaskStatus {
    NEW,
    IN_PROGRESS,
    DONE;

    @Override
    public String toString() {
        switch (name()) {
            case "NEW" -> {
                return "new";
            }
            case "IN_PROGRESS" -> {
                return "in progress";
            }
            case "DONE" -> {
                return "done";
            }
            default -> {
                return "";
            }
        }
    }
}
