package Messages.Server_side;

/**
 * Messaggio di successo del server
 * 
 * CHATLIST: lista delle chat presenti sul server
 * FRIENDLIST: lista degli amici dell'utente
 * LOGIN: info utente, lista chatroom e lista amici
 * REGISTRATION: info utente e lista chatroom
 * USERINFO: informazioni su un utente
 * CONNECTIONINFO: informazioni per lo scambio di file
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_success_message extends Gossip_server_message {
		public enum successMsg {CHATLIST, FRIENDLIST, LOGIN, REGISTRATION, USERINFO, CONNECTIONINFO, CHATINFO};
		public static final String SUCCESSMSG = "successmsg";
		
		protected Gossip_success_message.successMsg msg;
		
		@SuppressWarnings("unchecked")
		public Gossip_success_message(Gossip_success_message.successMsg m) {
			
			super(Gossip_server_message.Op.SUCCESS_OP);
			this.msg = m;
			jsonMsg.put(Gossip_success_message.SUCCESSMSG,  m.name());
			
		}
}
