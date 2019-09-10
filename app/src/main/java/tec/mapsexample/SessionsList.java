package tec.mapsexample;

import java.util.ArrayList;

public class SessionsList {

    ArrayList<Session> sessions;
    ArrayList<String> names;
    Session actual;

    private static final SessionsList ourInstance = new SessionsList();

    public static SessionsList getInstance() {
        return ourInstance;
    }

    private SessionsList() {
        sessions = new ArrayList<>();
        names = new ArrayList<>();
    }

    public void addSession(Session session){
        sessions.add(session);
        names.add(session.toString());
    }

    public void setActual(int i){
        actual = sessions.get(i);
    }

}
