package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.control.Controller;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.FallingToCenterGravityBuilder;
import simulator.factories.MassLossingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static Integer _dstepsDefaultValue = 150;
	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Integer _dsteps = 0;
	private static JSONObject _gravityLawsInfo = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<GravityLaws> _gravityLawsFactory;
	private static String mode;

	private static void init() {
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLossingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);

		ArrayList<Builder<GravityLaws>> gravityLawsBuilders = new ArrayList<>();
		gravityLawsBuilders.add(new NewtonUniversalGravitationBuilder());
		gravityLawsBuilders.add(new FallingToCenterGravityBuilder());
		gravityLawsBuilders.add(new NoGravityBuilder());
		_gravityLawsFactory = new BuilderBasedFactory<GravityLaws>(gravityLawsBuilders);
	}

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
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			parseOutFileOption(line);
			parseStepsOption(line);
			parseModeOption(line);
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

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// steps
		cmdLineOptions
				.addOption(Option.builder("s").longOpt("steps").hasArg().desc("Number of simulation steps.").build());

		// OUTPUT
		cmdLineOptions
				.addOption(Option.builder("o").longOpt("output").hasArg().desc("Bodies JSON output file.").build());
		// gravity laws -- there is a workaround to make it work even when
		// _gravityLawsFactory is null.
		//
		
		cmdLineOptions
		.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Printing mode.").build());
		
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		try {
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0)
					gravityLawsValues = gravityLawsValues + ", ";
				
				gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}
			defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).getString("type");
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
				.build());
		}catch(JSONException e)
		{
			
		}
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
	}

	private static void parseStepsOption(CommandLine line) throws ParseException {
		// TODO PARECIDO AL INFILEOPTION
		String s = line.getOptionValue("s", _dstepsDefaultValue.toString());
		try {
			_dsteps = Integer.parseInt(s);
			assert (_dsteps > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid steps value: " + s);
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static void parseGravityLawsOption(CommandLine line) throws ParseException {
		// this line is just a work around to make it work even when _gravityLawsFactory
		// is null, you can remove it when've defined _gravityLawsFactory
		if (_gravityLawsFactory == null)
			return;

		String gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gl.equals(fe.getString("type"))) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null)
				throw new ParseException("Invalid gravity laws: " + gl);

		} else
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
	}

	private static void startBatchMode() throws Exception {
		GravityLaws laws = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator sim = new PhysicsSimulator(laws, _dtime);
		Controller controller = new Controller(sim, _bodyFactory, _gravityLawsFactory);
		try {
		InputStream is = new FileInputStream(new File(_inFile));
		
		OutputStream os;

		if (_outFile == null)
			os = System.out;

		else
			os = new FileOutputStream(new File(_outFile));

		controller.loadBodies(is);
		controller.run(_dsteps, os);
		}catch(NullPointerException e)
		{
			System.out.println("Wrong arguments");
		}
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if(mode.equalsIgnoreCase("gui")) // Batch por defecto a no ser que se indique lo contrario
			startGUIMode();
		else
			startBatchMode();
	}
	
	public static void parseModeOption (CommandLine line)
	{
		 mode = "batch";
		if(line.hasOption("m"))
			mode = line.getOptionValue('m');
	}
	private static void startGUIMode() throws Exception
	{
		GravityLaws laws = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator sim = new PhysicsSimulator(laws, _dtime);
		Controller ctrl = new Controller(sim, _bodyFactory, _gravityLawsFactory);
		
		if(_inFile != null)
		ctrl.loadBodies(new FileInputStream(new File(_inFile)));
		
		
;		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		});
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}