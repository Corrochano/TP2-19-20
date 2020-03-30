package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
//import simulator.model.NewVehicleEvent;
import simulator.model.TrafficSimulator;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _sTicks;
	private static int _ticks = _timeLimitDefaultValue;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc(
				"Ticks to the simulator’s main loop (default value is 10).").build());
		
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void parseTicksOption(CommandLine line) throws ParseException {
		if(line.hasOption("t")) {
			_sTicks = line.getOptionValue("t");
			_ticks = Integer.parseInt(_sTicks);
		}
		else {
			_ticks = _timeLimitDefaultValue;
		}
		
	}
	
	private static <T> void initFactories() {

		// TODO complete this method to initialize _eventsFactory
		
		ArrayList<Builder<LightSwitchingStrategy>> lsbs= new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder("round_robin_lss") );
		lsbs.add( new MostCrowdedStrategyBuilder("most_crowded_lss") );
		Factory<LightSwitchingStrategy> lssFactory= new BuilderBasedFactory<>(lsbs);
		
		ArrayList<Builder<DequeuingStrategy>> dqbs= new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder("move_first_dqs") );
		dqbs.add( new MoveAllStrategyBuilder("move_all_dqs") );
		Factory<DequeuingStrategy> dqsFactory= new BuilderBasedFactory<>( dqbs);
		
		ArrayList<Builder<Event>> ebs= new ArrayList<>();
		ebs.add( new NewJunctionEventBuilder("new_junction", lssFactory,dqsFactory) );
		ebs.add( new NewCityRoadEventBuilder("new_city_road") );
		ebs.add( new NewInterCityRoadEventBuilder("new_inter_city_road") );
		// ...
		ebs.add(new NewVehicleEventBuilder("new_vehicle"));
		ebs.add(new SetContClassEventBuilder("set_cont_class")); // Estas no?
		ebs.add(new SetWeatherEventBuilder("set_weather"));
		// ...
		_eventsFactory= new BuilderBasedFactory<>(ebs);
		
		

	}

	private static void startBatchMode() throws IOException {
		// TODO complete this method to start the simulation
		InputStream is = new FileInputStream(new File(_inFile));
		OutputStream os = null;
		if(_outFile == null) {
			os = System.out;
		}
		else {
			os = new FileOutputStream(new File(_outFile));
		}
		TrafficSimulator tf = new TrafficSimulator();
		Controller control = new Controller(tf, _eventsFactory);
		control.loadEvents(is);
		control.run(_ticks, os);
		is.close();
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		startBatchMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
