Problem Statement:
To design a fuzzy expert system for stock trading where stock prices depend on two variables:MAD(Moving average divergences) coefficient(random value) and the current price of stock.
The equation governing the stock prices are:
p(i) =12+ 2.5sin(2πi/19)+0.8cos(2πi/5)+ζ(i) //Price on i-th day 
m(i) = 0.5cos(0.3i)−sin(0.3i)  //MAD Coefficient


Compilation Instruction:
The code can be compiled using the command java -jar fuzzy.jar.

SourceCode:
The source code is attached as fuzzy1.java

Output:
The output file created contains day by day transaction details as daily share price,totalassets,totalmoney and shares bought each day.
Negative sign in front of shares signify the selling operation otherwise its for buying.