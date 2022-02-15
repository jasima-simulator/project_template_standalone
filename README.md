# jasima Standalone Project Template

This project is a Java project to get started with [jasima](https://github.com/jasima-simulator/jasima-simcore), the Java Simulator for Manufacturing and Logistics. It contains a binary version of jasima and its depedencies in the `lib` folder (currently: SNAPSHOT version from 2022-02-15). 

It is intended to provide self-contained examples of core concept. For more advanced usage scenarios you probably want to check out the actual jasima repository and make use of Java build tools like [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/) to manage dependencies.

## Installation and Usage

This project contains a self-contained Java project created with the Java IDE [Eclipse](https://www.eclipse.org/downloads/). To use it, check it out from GitHub and import it as a Java project into Eclipse (this should actually work with any Java IDE).

When everything worked as expected you should be able to run the `HelloWorld` example contained in `src/examples/simulation/HelloWorld.java` producing the following output on the console:

	simulation main process started at: 0.0
	slowClock() starting at: 0.0
	fastClock() starting at: 0.0
	fastClock() executed at: 3.0
	simulation main process finished at: 5.0
	fastClock() executed at: 6.0
	slowClock() executed at: 7.0
	fastClock() executed at: 9.0
	fastClock() executed at: 12.0
	slowClock() executed at: 14.0
	fastClock() executed at: 15.0
	fastClock() executed at: 18.0

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change. Please use the jasima issue tracker at [GitHub](https://github.com/jasima-simulator/jasima-simcore/issues) for this. 

Please consider adding tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/). Please note that this is the license of this example project. jasima and the third-party libraries it uses might be distributed under different license terms.

[jasima](http://jasima.org/) is a registered trademark of [SimPlan AG](https://www.simplan.de/), Hanau, Germany.