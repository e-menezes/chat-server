/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.util.ArrayList;

/**
 *
 * @author menezes
 */
public class Cliente {
    public ArrayList<Mensagem> mensagens;
    public int nrProximaMsg;
    
    public Cliente(){
        mensagens = new ArrayList<Mensagem>();
    }
    
    public boolean hasMessages(){
        return !mensagens.isEmpty();
    }
}
