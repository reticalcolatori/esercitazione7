import java.rmi.RemoteException;

public interface RegistryRemotoTagServer extends RegistryRemotoServer, RegistryRemotoTagClient {

    //associaTag(nome_logico_server, tag)

    /**
     * Consente l'associazione del nome logico ad uno dei tag disponibili. (Proposta.pdf Pag 2)
     *
     * @param nome_logico_server Nome del servizio a cui associare il tag
     * @param tag tag da associare. Deve essere un tag disponibile.
     * @return Restituisce vero se il tag è stato associato.
     * @throws RemoteException Lanciata in caso di errore
     */
    public boolean associaTag(String nome_logico_server, String tag) throws RemoteException;
}
