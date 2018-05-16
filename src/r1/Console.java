package r1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import agent.AgentLexer;
import agent.AgentParser;
import br.ufsc.ine.agent.Agent;
import br.ufsc.ine.agent.context.beliefs.BeliefsContextService;
import br.ufsc.ine.agent.context.communication.CommunicationContextService;
import br.ufsc.ine.parser.AgentWalker;
import br.ufsc.ine.parser.VerboseListener;
import sensor.LookEnv;

public class Console {

	private static void startAgent(){
        try {

            File agentFile = new File("r1.on");
            CharStream stream = CharStreams.fromFileName(agentFile.getAbsolutePath());
            AgentLexer lexer = new AgentLexer(stream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            AgentParser parser = new AgentParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new VerboseListener());

            ParseTree tree = parser.agent();
            ParseTreeWalker walker = new ParseTreeWalker();

            AgentWalker agentWalker = new AgentWalker();
            walker.walk(agentWalker, tree);

            Agent agent = new Agent();
            agent.run(agentWalker);




        } catch (IOException e) {
            System.out.println("I/O exception.");
        }
    }
	
	public static void main(String[] args) {
		startAgent();
		 
		while (true) {
			Scanner scanIn = new Scanner(System.in);
			String inputString = scanIn.nextLine();
			LookEnv.envObservable.onNext(inputString);
			//System.out.println(BeliefsContextService.getInstance().getTheory());
		}
	}
	
}
