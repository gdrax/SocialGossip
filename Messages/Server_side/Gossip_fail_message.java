package Messages.Server_side;

/**
 * Messaggio di errore del server
 * 
 * UNKNOWNREQUEST: Richiesta sconosciuta
 * NICKALREADY: Username già registrato
 * NICKUNKNOWN: Username non registrato
 * CHATALREADY: chat già esistente
 * CHATUNKNOWN: chat inesistente
 * OPNOTALLOWED: operazione non permessa (eliminare una chat di altri)
 * FRIENDALREADY: già amici
 * FRINEDOFFLINE: amico non collegato
 * WRONGPASSWORD: password errata
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_fail_message extends Gossip_server_message {
	public enum failMsg {UNKNOWN_REQUEST, NICKALREADY, NICKUNKNOWN, CHATALREADY, CHATUNKNOWN, OPNOTALLOWED, FRIENDOFFLINE, FRIENDALREADY, WRONGPASSWORD};
	public static final String FAILMSG = "failmsg";
	
	protected Gossip_fail_message.failMsg msg;
	
	@SuppressWarnings("unchecked")
	public Gossip_fail_message(Gossip_fail_message.failMsg m) {
		
		super(Gossip_server_message.Op.FAIL_OP);
		this.msg = m;
		jsonMsg.put(Gossip_fail_message.FAILMSG,  m.name());
	}
}
