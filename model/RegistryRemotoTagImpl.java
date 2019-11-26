package model;
/**
 * 	Implementazione del Registry Remoto.
 *	Metodi descritti nelle interfacce.  
 */

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RegistryRemotoTagImpl extends UnicastRemoteObject implements
	RegistryRemotoTagServer {

	// num. entry [nomelogico][ref]
	final int tableSize = 100;
	final int maxTags = 5;

	// Tabella: la prima colonna contiene i nomi, la seconda i riferimenti remoti
	Object[][] table = new Object[tableSize][maxTags + 2];
	
	// Tag consentiti
	String tags[] = {
		"IT",
		"ATTUALITA",
		"GIARDINAGGIO",
		"MODA",
		"CUCINA",
		"SPORT"
	};

	public RegistryRemotoTagImpl() throws RemoteException {
		super();
		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < maxTags + 2; j++) {
				table[i][j] = null;
			}
		}
	}

	/** Aggiunge la coppia nella prima posizione disponibile */
	public synchronized boolean aggiungi(String nomeLogico, Remote riferimento)
			throws RemoteException {
		// Cerco la prima posizione libera e la riempio
		boolean risultato = false;
		if( (nomeLogico == null) || (riferimento == null) )
			return risultato;
		for (int i = 0; i < tableSize; i++)
			if (table[i][0] == null) {
				table[i][0] = nomeLogico;
				table[i][1] = riferimento;
				risultato = true;
				break;
			}
		return risultato;
	}

  /** Restituisce il riferimento remoto cercato, oppure null */
	public synchronized Remote cerca(String nomeLogico) throws RemoteException {
		Remote risultato = null;
		if( nomeLogico == null ) return null;
		for (int i = 0; i < tableSize; i++)
			if ( nomeLogico.equals((String) table[i][0]) ) {
				risultato = (Remote) table[i][1];
				break;	
			}
		return risultato;
	}

	/** Restituisce tutti i riferimenti corrispondenti ad un nome logico */
	public synchronized Remote[] cercaTutti(String nomeLogico)
			throws RemoteException {
		int cont = 0;
		if( nomeLogico == null ) return new Remote[0];
		for (int i = 0; i < tableSize; i++)
			if ( nomeLogico.equals((String) table[i][0]) )
				cont++;
		Remote[] risultato = new Remote[cont];
		// Ora lo uso come indice per il riempimento
		cont = 0;
		for (int i = 0; i < tableSize; i++)
			if ( nomeLogico.equals((String) table[i][0]) )
				risultato[cont++] = (Remote) table[i][1];
		return risultato;
	}

	/** Restituisce tutti i riferimenti corrispondenti ad un nome logico */
	public synchronized Object[][] restituisciTutti() throws RemoteException {
		int cont = 0;
		for (int i = 0; i < tableSize; i++)
			if (table[i][0] != null) cont++;
		Object[][] risultato = new Object[cont][2];
		// Ora lo uso come indice per il riempimento
		cont = 0;
		for (int i = 0; i < tableSize; i++)
			if (table[i][0] != null) {
				risultato[cont][0] = table[i][0];
				risultato[cont][1] = table[i][1];
			}
		return risultato;
	}

	/** Elimina la prima entry corrispondente al nome logico indicato */
	public synchronized boolean eliminaPrimo(String nomeLogico)
			throws RemoteException {
			boolean risultato = false;
			if( nomeLogico == null ) return risultato;    
			for (int i = 0; i < tableSize; i++)
				if ( nomeLogico.equals((String) table[i][0]) ) {
					table[i][0] = null;
					table[i][1] = null;
					risultato = true;
					break;
				}
			return risultato;
	}

	public synchronized boolean eliminaTutti(String nomeLogico)
			throws RemoteException {
		boolean risultato = false;
		if( nomeLogico == null ) return risultato;    
		for (int i = 0; i < tableSize; i++)
			if ( nomeLogico.equals((String) table[i][0]) ) {
				if (risultato == false)
        	risultato = true;
				table[i][0] = null;
				table[i][1] = null;
			}
		return risultato;
	}
	
	@Override
	public synchronized boolean associaTag(String nomeLogico, String tag) throws RemoteException {
		boolean result = false;
		boolean tagCorrect = false;
		
		if(nomeLogico == null || tag == null) throw new RemoteException("Uno dei parametri è null");
		//if(!Arrays.asList(tags).contains(tag)) throw new RemoteException("Tag non consentito");
		for (String currTag : tags) {
			if(currTag.equals(tag)){
				tagCorrect = true;
				break;
			}
		}
		
		if(!tagCorrect) throw new RemoteException("Tag non consentito");
		
		for (int i = 0; i < tableSize; i++) {
			if ( nomeLogico.equals((String) table[i][0]) ) {
				
				for (int j = 2; j < maxTags + 2; j++) {
					if(table[i][j] == null) {
						if (!result) 
							result = true;
						
						table[i][j] = (String) tag;
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public synchronized String[] cercaTag(String tag) throws RemoteException {
		int cont = 0;
		boolean tagCorrect = false;
		
		for (String currTag : tags) {
			if(currTag.equals(tag))
				tagCorrect = true;
		}
		
		if(!tagCorrect) throw new RemoteException("Tag non consentito");
		
		if( tag == null ) return new String[0];

		for (int i = 0; i < tableSize; i++) {
			for (int j = 2; j < maxTags + 2; j++) {
				if ( tag.equals((String) table[i][j]) ) {
					cont++;
					break;
				}				
			}
		}
			
		String[] risultato = new String[cont];
		// Ora lo uso come indice per il riempimento
		cont = 0;
		for (int i = 0; i < tableSize; i++) {
			for (int j = 2; j < maxTags + 2; j++) {
				if ( tag.equals((String) table[i][j]) ) {
					risultato[cont++] = (String) table[i][0];
					break;
				}
			}
		}
		return risultato;
	}

	// Avvio del Server RMI
	public static void main(String[] args) {

		int registryRemotoPort = 1099;
		String registryRemotoHost = "localhost";
		String registryRemotoName = "RegistryRemoto";

		// Controllo dei parametri della riga di comando
		if (args.length != 0 && args.length != 1) {
			System.out.println("Sintassi: ServerImpl [registryPort]");
			System.exit(1);
		}
		if (args.length == 1) {
			try {
				registryRemotoPort = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out
				.println("Sintassi: ServerImpl [registryPort], registryPort intero");
				System.exit(2);
			}
		}

		// Impostazione del SecurityManager
		if (System.getSecurityManager() == null)
			System.setProperty("java.security.policy", "rmi.policy");
			System.setSecurityManager(new RMISecurityManager());

		// Registrazione del servizio RMI
		String completeName = "//" + registryRemotoHost + ":" + registryRemotoPort
				+ "/" + registryRemotoName;
		try {
			RegistryRemotoTagImpl serverRMI = new RegistryRemotoTagImpl();
			Naming.rebind(completeName, serverRMI);

			System.out.println("Server RMI: Servizio \"" + registryRemotoName
					+ "\" registrato");
		} catch (Exception e) {
			System.err.println("Server RMI \"" + registryRemotoName + "\": "
					+ e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

}