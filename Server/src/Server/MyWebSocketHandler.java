package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


@WebSocket
public class MyWebSocketHandler {
	 public  static LinkedList<Session>WaitRoom;
	 public  static ArrayList<Room> Rooms;
	 @OnWebSocketClose
	    public void onClose(Session session,int statusCode, String reason) {
	        System.out.println("\033[37mSesion: "+session.hashCode()+", Close: statusCode=" + statusCode + ", reason=" + reason);
	    
	   if(Rooms.size()>0) {
	   int userS=buscarUserInRoom(session) ;
	   Room r= Rooms.get(userS);
	   if(r.getUser1().hashCode()==session.hashCode()) {
		   try {
			r.getUser2().getRemote().sendString("error;Tú oponente abandonó la sala");
			r.getUser2().disconnect();
			Rooms.remove(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("\033[31mError al desconectar a jugador 2"+e.getMessage());
		}
	
	   }
	   if(r.getUser2().hashCode()==session.hashCode()) {
		   try {
			r.getUser1().getRemote().sendString("error;Tú oponente abandonó la sala");
			r.getUser1().disconnect();
			Rooms.remove(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("\033[31mError al desconectar a jugador 1"+e.getMessage());
		}		   
	   }
	   }else {
		   WaitRoom.remove(session);
	   }	   
	 }

	    @OnWebSocketError
	    public void onError(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }

	    @OnWebSocketConnect
	    public void onConnect(Session session) {
	        System.out.println("\033[37mConnect: " + session.getRemoteAddress().getAddress());
	        try {
	        	if(Rooms==null) {
	        		Rooms = new ArrayList<Room>();
	        		WaitRoom= new LinkedList<Session>();
	        	}
	        	WaitRoom.add(session);     
	            session.getRemote().sendString("success;Conexión éxitosa;Su id de sesión es "+session.hashCode());
	            if(WaitRoom.size()>=2) {
	            	int nwait=0;
	            	if(WaitRoom.size()%2==0) {
	            		nwait=WaitRoom.size();
	            	}else {
	            		nwait=WaitRoom.size()-1;
	            	}
	            	for (int i = 0; i < nwait/2; i++) {
	            		Session j1= WaitRoom.pop();
	            		Session j2= WaitRoom.pop();
	            		Room r= new Room(j1, j2);
	            		j1.getRemote().sendString("fid;"+0);
	            		j1.getRemote().sendString("User;"+j1.hashCode());
	            		j2.getRemote().sendString("fid;"+1);
	            		j2.getRemote().sendString("User;"+j2.hashCode());
		            	Rooms.add(r);
					}		            	
	            }	            
	            System.out.print("\033[36mUsuarios en espera: "+WaitRoom.size()+" ");
            	System.out.println("\033[34mRooms actuales : "+Rooms.size());
	        } catch (IOException e) {
	        	System.out.println("\033[31mConexión errror"+e.getMessage());
	        }
	    }
	    public static int buscarUserInRoom(Session session) {
	    	int i=0;
	    	while (i<Rooms.size()) {
	    		if(Rooms.get(i).getUser1().hashCode()==session.hashCode()) {
	    			return i;
	    		}else if (Rooms.get(i).getUser2().hashCode()==session.hashCode()) {
	    			return i;
	    		}else {
				i++;				
				}
			}
	    	return 999999;
	    }
	    public static void enviarMensaje(Room r, Session s,String m)  {
	    	if(s.hashCode()==r.getUser1().hashCode()) {
	    		try {
					r.getUser2().getRemote().sendString(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("\033[31mEnviar mensaje errror"+e.getMessage());
				}
	    	}else {
	    		try {
					r.getUser1().getRemote().sendString(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("\033[31mEnviar mensaje errror"+e.getMessage());
				}
	    	}
	    }
	    @OnWebSocketMessage
	    public void onMessage(Session session, String message) {
	    	 System.out.println("\033[35mMessage: " + message+" ");
	    	 
	    	if(Rooms.size()>0) {
	    		int user =buscarUserInRoom(session);
	    		if(user==999999) {
	    			try {
						session.getRemote().sendString("Aun no hay partida");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Room mensaje errror"+e.getMessage());
					}
	    		}else {
	    			enviarMensaje(Rooms.get(user),session,message);
	    	}
	    		}	       
	        
	    }
}
