package ontologicExperiment;

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import agent.AgentLexer;
import agent.AgentParser;
import br.ufsc.ine.agent.Agent;
import br.ufsc.ine.agent.context.ContextService;
import br.ufsc.ine.agent.context.beliefs.BeliefsContextService;
import br.ufsc.ine.agent.context.communication.CommunicationContextService;
import br.ufsc.ine.agent.context.desires.DesiresContextService;
import br.ufsc.ine.agent.context.intentions.IntentionsContextService;
import br.ufsc.ine.agent.context.ontologic.OntologicContextService;
import br.ufsc.ine.agent.context.plans.PlansContextService;
import br.ufsc.ine.parser.AgentWalker;
import br.ufsc.ine.parser.VerboseListener;
import ontologicExperiment.actuators.BuildQuestion;
import ontologicExperiment.actuators.PersistBelief;
import ontologicExperiment.sensors.AnswerSensor;

public class Main {

	
	private AnswerSensor answerSensor;
	private BuildQuestion buildQuestion;
	private PersistBelief persistBelief;
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		startAgent();
		int total = 100;
		long startTime = 0;  	  	
		for (int i = 0; i < total; i++) {
			//startTime = System.nanoTime();
			//Thread.sleep(5000);
			percept(i);
			//reasoningCycle(startTime, i+1);
			
		}
	}

	private static void startAgent() {
		try {

			File agentFile = new File("/home/leon/Documentos/Git/sigon-examples/src/ontologicExperiment/agent.on");
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
			
			OntologicContextService ontologicContextService = OntologicContextService.getInstance();
			ContextService[] cc = new ContextService[] {ontologicContextService};
			
			Agent agent = new Agent();
			agent.run(agentWalker,cc);

		} catch (IOException e) {
			System.out.println("I/O exception.");
		}
	}
	
	private static void percept(int index) {

		AnswerSensor.answerObservable.onNext("d" + index + ".");
		

		System.out.println("CC " + CommunicationContextService.getInstance().getTheory());
		System.out.println("BC " + BeliefsContextService.getInstance().getTheory().toString());
		System.out.println("DC " + DesiresContextService.getInstance().getTheory());
		System.out.println("PC " + PlansContextService.getInstance().getTheory().toString());
		System.out.println("IC " + IntentionsContextService.getInstance().getTheory());
		System.out.println("CO " + OntologicContextService.getInstance().getTheory());

	}
}
