# Servidor WebSocket
Para el correcto funcionamiento del proyecto **Servidor WebSocket** se requiere importar la libreria [Jetty](https://github.com/agarcia196/Servidor-WebSocket/tree/master/jetty-all-9.4.15.v20190215-uber) 

# Configuración incluida

  - Conexión para clientes *WebSocket*.
  - Sala de espera para las sesiones.
  - Creación de rooms para comunicación 1 a 1, los rooms se generan para almacenar y comunicar 2 sesiones.
  - Desconexión de sesiones si un usuario abandona el Room.
### Instalación

Abrir el proyecto en un IDE como [Eclipse](https://www.eclipse.org/) v2019-03.

Ingrese a la clase *WebSocker.java* y asegurese de que el puerto por donde correra el servidor concuerde con el que desea en sus clientes WebSocket.
```sh
Server server = new Server(8080);
```
Ejecutando esta misma clase se inicia el servidor.

### Clase MyWebSocketHandler.java
Cuando se finaliza una sesion se hace uso de este metodo.
```sh
 @OnWebSocketClose
	    public void onClose(){}
```
Cuando un cliente *WebSocket* se conecta hace uso del siguiente metodo.
```sh
@OnWebSocketConnect
	    public void onConnect(Session session){} 
```	    
Cuando un cliente *WebSocket* envia un mensaje al servidor el siguiente metodo se modificó para que funcione como redireccionador en los rooms en caso de estar en uno
```sh
@OnWebSocketMessage
	    public void onMessage(Session session, String message){}
```	    	    
