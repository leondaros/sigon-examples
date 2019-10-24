package ontologicExperiment.actuators;

import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufsc.ine.agent.context.communication.Actuator;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BuildQuestion extends Actuator{
		
	@Override
	public void act(List<String> args) {
		System.out.println("ACTION");
	}
	
	public static void connect() throws URISyntaxException{
		
		Socket socket = IO.socket("http://127.0.0.1:3001");
		socket.on("answer", new Emitter.Listener() {
			public void call(Object... args) {
				JSONObject obj = (JSONObject)args[0];
				System.out.println(obj);
			}
		});	
		
		// Sending an object
		JSONObject obj = new JSONObject();
//		JSONArray array = new JSONArray(); 
		String[] list = {"country(valletta,malta).","country(birkirkara,teste).",
		               "country(south_Eastern_Region,teste2).","country(malta_(island),teste3)."};
		try {
			obj.put("answers", list);
			obj.put("rightAnswer", "country(valletta,malta).");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(obj);
		socket.emit("sendQuestion", obj);
		socket.connect();
	}
	
}
