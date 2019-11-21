import java.rmi.RemoteException;

public interface RegistryRemotoTagClient extends RegistryRemotoClient {

    //CercaTag(tag)

    /**
     * Serve per cercare i nomi di servizio avente il tag.
     *
     * @param tag tag che deve avere il servizio
     * @throws
     * @return array con i nomi che rispettano il tag.
     */
    public String[] cercaTag(String tag) throws RemoteException;
}
