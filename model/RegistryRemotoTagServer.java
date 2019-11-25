package model;
import java.rmi.RemoteException;

public interface RegistryRemotoTagServer extends RegistryRemotoServer, RegistryRemotoTagClient {
	public boolean associaTag(String nomeLogico, String tag) throws RemoteException;
}
