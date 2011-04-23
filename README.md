Financial Moving Average Tester [BETA]
=============

Open source software for testing multiple moving average strategies applied in stock markets.

** still under development and improvement **

Overview
--------

This software was developed for my bachelors final paper. Works under Java and [AgentSpeak](http://en.wikipedia.org/wiki/AgentSpeak) 
,an Agent Programming Language interpreted to Java with [Jason](http://jason.sourceforge.net/Jason/Jason.html).

The methodology applied is based in agents, asynchronously running different moving average strategies against historical stocks quotes. 
Check [investopedia](http://www.investopedia.com/university/movingaverage/movingaverages4.asp) for good definition about these strategies.

### Moving Average Types

There are 2 types of moving average implemented in this project: [Simple Moving Average (SMA)](http://www.investopedia.com/terms/s/sma.asp) 
and [Exponential Moving Average (EMA)](http://www.investopedia.com/terms/e/ema.asp) 

### Strategies

* Price Crossover: given a moving average, when the stock quote crosses from below to above the average, the buy decision is generated.
When the stock quote crosses from above to below the average, it generates the sell decision.

* Averages Crossover: given 2 averages, one faster than another (e.g. SMA(13) and SMA(45)), when the fast average crosses the slow average
from below to above, the buy decision is generated. When the fast average crosses the slow average from above to below, it generates the sell decision.

Installation
------------

### Regular

Download [.jar package](https://github.com/brunotavares/moving_average_tester/tree/master/moving_average_tester.jar) and place into a folder (e.g. `/movingAverageTester`). Under this folder create a folder called `quotes` (e.g. `/movingAverageTester/quotes`).
Place the historical stock quotes .xls files in it. You can [download some sample files from here](https://github.com/brunotavares/moving_average_tester/tree/master/quotes).

### Developer

`git clone git://github.com/brunotavares/moving_average_tester.git`

Import eclipse project and run, selecting `MercadoEnvironment` as start up class.

Contributing
------------

1. Fork it.
2. Create a branch (`git checkout -b my_strategy`)
3. Commit your changes (`git commit -am "Added New Strategy"`)
4. Push to the branch (`git push origin my_strategy`)
5. Create an [Issue](https://github.com/brunotavares/moving_average_tester/issues) with a link to your branch