package managing;

public class Managers {
    static TaskManager manager;
    public static TaskManager getDefault(String type){
        return manager;
    };
}
