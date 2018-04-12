package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Messages.Gossip_parser;


/**
 * Traduttore dei messaggi
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_translator {

	private final static String prefix = "https://api.mymemory.translated.net/get?";
	
	public static String translate(String text, String senLanguage, String recLanguage) throws IOException, ParseException {
		if (text == null || senLanguage == null || recLanguage == null)
			throw new NullPointerException();
		
		//le sigle devono essere lunghe esattamente due caratteri
		if (senLanguage.length() != 2 || recLanguage.length() != 2)
			throw new IllegalArgumentException();
		
		//lingue uguali
		if (senLanguage == recLanguage)
			return text;
		
		//sostituisco gli spazi che sono presenti nel testo da tradurre
		String spacelessText = text.replace(" ",  "%20");
		
		//creo string che corrisponde alla query
		String request = prefix+"q="+spacelessText+"&langpair="+senLanguage+"|"+recLanguage;
		
		//creo URL
		URL requestURL = new URL(request);
		
		//ottengo una URLConnection
		URLConnection connection = requestURL.openConnection();
		
		//creao reader pepr lo stream
		BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		//leggo la risposta
		String line = null;
		String reply = "";
		while ((line = input.readLine()) != null) {
			reply = reply+line;
		}
		
		//estraggo il testo tradotto
		JSONObject JSONReply = Gossip_parser.getJsonObject(reply);
		return Gossip_parser.getTransatedText(JSONReply);
	}
}
