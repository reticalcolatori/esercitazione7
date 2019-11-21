import java.rmi.RemoteException;

public interface RegistryRemotoTagServer extends RegistryRemotoServer {

    //associaTag(nome_logico_server, tag)
    public boolean associaTag(String nome_logico_server, String tag) throws RemoteException;
}
