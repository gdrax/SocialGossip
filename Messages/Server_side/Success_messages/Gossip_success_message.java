package Messages.Server_side.Success_messages;

import Messages.Server_side.Gossip_server_message;

/**
 * Messaggio di successo del server
 * 
 * LOGIN: info utente, lista chatroom e lista amici (vedi Gossip_info_login.java)
 * REGISTRATION: info utente e lista chatroom (vedi Gossip_info_registration.java)
 * USERINFO: informazioni su un utente (vedi Gossip_userinfo_message.java)
 * CONNECTIONINFO: informazioni per lo scambio di file (vedi Gossip_connection_info_message.java)
 * CHATINFO: informazioni su una chatroom (vedi Gossip_chat_info.java)
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_success_message extends Gossip_server_message {
		public enum successMsg {LOGIN, REGISTRATION, USERINFO, CONNECTIONINFO, CHATINFO};
		public static final String SUCCESSMSG = "successmsg";
		
		protected Gossip_success_message.successMsg msg;
		
		@SuppressWarnings("unchecked")
		public Gossip_success_message(Gossip_success_message.successMsg m) {
			
			super(Gossip_server_message.Op.SUCCESS_OP);
			this.msg = m;
			jsonMsg.put(Gossip_success_message.SUCCESSMSG,  m.name());
			
		}
}
