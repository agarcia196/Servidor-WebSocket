package Server;

import java.util.ArrayList;
import org.eclipse.jetty.websocket.api.Session;

public class Room {
	
	private static int count=0;
	private int id;
	private Session user1;
	private Session user2;
	private ArrayList<String> mensajes;
	public Room(Session user1, Session user2) {
		super();
		this.id = count++;
		this.user1 = user1;
		this.user2 = user2;
		this.mensajes =new ArrayList<String>();
	}
	public ArrayList<String> getMensajes() {
		return mensajes;
	}
	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}
	public int getId() {
		return id;
	}
	public Session getUser1() {
		return user1;
	}
	public Session getUser2() {
		return user2;
	}
	public void setUser1(Session user1) {
		this.user1 = user1;
	}
	public void setUser2(Session user2) {
		this.user2 = user2;
	}
	
	
}
