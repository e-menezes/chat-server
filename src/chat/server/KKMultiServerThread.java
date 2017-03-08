/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.server;

import chat.Cliente;
import chat.Mensagem;
import chat.KnockKnockProtocol;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketException;
import java.util.Hashtable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;


/**
 *
 * @author menezes
 */
public class KKMultiServerThread extends Thread {

    static Hashtable<String, Cliente> storage = null;
    
    private Socket socket = null;
    KnockKnockProtocol kkp;
    public KKMultiServerThread(Socket socket) {
        super("KKMultiServerThread");
        this.socket = socket;
        if(storage == null)
            storage = new Hashtable<String, Cliente>();
    }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine;
            kkp = new KnockKnockProtocol();

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Servidor recebeu: " + inputLine);
                JSONObject response = decode(inputLine);
                System.out.println("Servidor enviou: " + response);
                out.println( response );
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("ERRO: IOException " + e.getMessage());
        }
    }
    
    public JSONObject serverResponse(String msg, int msgNr){
        JSONObject jsonObject = new JSONObject()
            .element( "id", "0" ) // id:0 para servidor
            .element( "data", msg )  
            .element( "msgNr", ++msgNr );
        return jsonObject;
    }
    
    public JSONObject decode(String msg){
        JSONObject obj = JSONObject.fromObject(msg);
        if(obj.isNullObject())
            return null;
        int msgNr = obj.getInt("msgNr");
        String id = obj.getString("id");
        
        JSONObject rsp = new JSONObject();
        
        switch( obj.getString("cmd") ){
            case "login":
                rsp.element( "id", "0" ) // id:0 para servidor
                .element( "msgNr", ++msgNr );
                if( !storage.containsKey(id) ){
                    storage.put(id, new Cliente());
                } else {
                    Cliente c = storage.get(id);
                    if( c.hasMessages() ){
                        Object o[] = c.mensagens.toArray();
                        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON( o );
                        rsp.put("data", jsonArray);
                    }
                }
                break;
            case "logout":
                break;
            case "enviar":
                String dst = obj.getString("dst");
                String data = obj.getString("data");
                String serverResponse = null;
                // resposta do servidor
                rsp.element( "id", "0" ) // id:0 para servidor
                    .element( "msgNr", ++msgNr );
                // buscando o destinatário
                Cliente c = null;

                if( dst.matches("0")){
                    serverResponse = kkp.processInput(data);
                    rsp.element( "id", "0" ) // id:0 para servidor 
                        .element( "msgNr", ++msgNr );
                    if( !storage.containsKey(id) ){
                        c = new Cliente();
                    } else {
                        c = storage.get(id);
                    }
                    c.mensagens.add(new Mensagem("0", serverResponse));
                    storage.put(id, c);
                    System.out.println("Servidor armazenou: " + serverResponse + " De: 0 Para: " + id);
                } else {
                    if( !storage.containsKey(dst) ){
                        c = new Cliente();
                    } else {
                        c = storage.get(dst);
                    }
                    c.mensagens.add(new Mensagem(id, data));
                    storage.put(dst, c);
                    System.out.println("Servidor armazenou: " + data + " De: " + id + " Para: " + dst);
                }
                break;
            case "receber":
                rsp.element( "id", "0" ) // id:0 para servidor 
                    .element( "msgNr", ++msgNr );
                if( storage.containsKey(id) ){
                    c = storage.get(id);
                    Object o[] = c.mensagens.toArray();
                    JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON( o );
                    rsp.put("data", jsonArray);
                    c.mensagens.clear();
                    if( o.length == 0 )
                        System.out.println("Cliente "+ id + " Buscou: NADA" );
                    else {
                        System.out.println("Cliente "+ id + " Buscou: " + rsp.optJSONArray("data"));
                    }
                } else {
                    
                    
                }
                break;
        }
        
        return rsp;
    }

    
}
