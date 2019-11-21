import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RegistryRemotoTagImpl extends UnicastRemoteObject implements RegistryRemotoTagServer {

    // num. entry [nomelogico][ref]
    final int tableSize = 100;

    // Tabella: la prima colonna contiene i nomi, la seconda i riferimenti remoti, la terza ai tag.
    Object[][] table = new Object[tableSize][3];

    public RegistryRemotoTagImpl() throws RemoteException {
        super();
        for (int i = 0; i < tableSize; i++) {
            table[i][0] = null;
            table[i][1] = null;
            table[i][2] = null;
        }
    }

    //Vecchio metodo preso dalla svolta.
    /** Aggiunge la coppia nella prima posizione disponibile */
    @Override
    public synchronized boolean aggiungi(String nomeLogico, Remote riferimento) throws RemoteException {
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

    //Vecchio metodo preso dalla svolta.
    /** Restituisce tutti i riferimenti corrispondenti ad un nome logico */
    @Override
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

    //Vecchio metodo preso dalla svolta.
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

    //Vecchio metodo preso dalla svolta.

    @Override
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

    //Vecchio metodo preso dalla svolta.
    /** Restituisce il riferimento remoto cercato, oppure null */
    @Override
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

    //Vecchio metodo preso dalla svolta.
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

    @Override
    public boolean associaTag(String nome_logico_server, String tag) throws RemoteException {
        return false;
    }

    @Override
    public String[] cercaTag(String tag) throws RemoteException {
        return new String[0];
    }

    // Avvio del Server RMI
    public static void main(String[] args) {

        int registryRemotoPort = 1099;
        String registryRemotoHost = "localhost";
        String registryRemotoName = "RegistryRemotoTag";

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
