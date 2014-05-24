import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class FuzzySet1
{
	double slope1,slope2;
	double c1,c2;
	double min,max;
	boolean slicing = false;
	double topvalue = 1.0;
	double[] membershipvalues = new double[10];
	
	FuzzySet1(double x1,double x2)
	{
		min = x1;
		max = x2;
		if (x2-x1 >= 0.2)
		{
			slope1=10;
			slope2=-10;
			c1=-(slope1 * x1);
			c2=-(slope2 * x2);	
		}
		else
		{
			slope1 = (x2-x1)/20;
			slope2 = -(x2-x1)/20;
			c1 = -(slope1 * x1);
			c2 = -(slope2 * x2);
		}
	}
	
	void CutAt(double x)
	{
		if(slicing == false)
		{
			topvalue = x;
			slicing = true;
		}
		else
		{
			if(topvalue > x)
			{}
			else
			{
				topvalue = x;
			}
		}
		
	}
	
	double Membership(double x)
	{
		double result;
		if( x<min || x>max)
		{
			return 0.0;
		}
		else
		{
			if(x>=(min + 0.1) && (x <= (max - 0.1)))
			{
//				result = (slope2*x) + c2;
				return topvalue;
			}
			else if(x>=(min + 0.1))
			{
				result = (slope2 * x) + c2;
				return Math.min(result, topvalue);
			}
			else
			{
				result = (slope1 * x) + c1;
				return Math.min(result,topvalue);
			}
		}
		
	}
}




public class fuzzy1 {

	/**
	 * @param args
	 */
	
	static double totalmoney = 10000;
	static int numberofshares = 0;
	static double currentpriceofshare =0;
	static void trade(int x)
	{
		if ((totalmoney > (currentpriceofshare * x)) || x < 0 )
		{
			if(x>0)
			{totalmoney = totalmoney - (currentpriceofshare * x);
			numberofshares = numberofshares + x;
			}
			else if(-x > numberofshares)
			{totalmoney = totalmoney + (currentpriceofshare * numberofshares);
			numberofshares = numberofshares -numberofshares;}
			else
			{totalmoney = totalmoney - (currentpriceofshare * x);
			numberofshares = numberofshares + x;}
		}
		else
		{
			int tradingnumber = (int) Math.floor(totalmoney/currentpriceofshare);
			numberofshares += tradingnumber;
			totalmoney = totalmoney - (currentpriceofshare * tradingnumber);
		}
	}
	
	static double price(int x)
	{
		double Degree1 = (360*x)/19;
		double Degree2 = (360*x)/5;
		double r = (Math.random() * 2) - 1;
		double random = 8*r*(x%2);
		double value = 12 + (2.5*(Math.sin(Degree1))) + (0.8 * (Math.cos(Degree2))) + random;
		return value;
		
	}
	
	static double mad(int x)
	{
		double Degree1 = 0.3 * x;
		double value = 0.5*(Math.cos(Degree1)) - Math.sin(Degree1);
		return value;
	}
	
	static double normalizeprice(double x)
	{
		double max = 23;
		double min = 2;
		if(x > max)
		{
			max=x;
			return 1;
		}
		else if(x < min)
		{
			min = x;
			return 0;
		}
		else
		{
			return ((x - min)/(max-min));
		}
		
	}
	
	static double normalizemad(double x)
	{
		double max = 1.12;
		double min = -1.12;
		if(x >max)
		{
			max = x;
			return 1.0;
		}
		else if(x < min)
		{
			min = x;
			return 0.0;
		}
		else
		{
			return ((x - min)/(max-min));
		}
		
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	static int defuzzifyshares(double x)
	{
		return (int) ((round(x,2)-0.5)*700);	
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int count=0;
//		double[] weights = new double[11];
		PrintWriter out = new PrintWriter(new FileWriter("output.txt"), true);
	    double totalassets = 0;
		totalmoney = 10000;
		numberofshares = 0;
		currentpriceofshare =0;
		double dailyprofit = 0,totalprofit =0;
		double lastamount = 10000;
		double currentamount = 10000;
		
		for(int i=1;i<150;i++)
		{
			//Creating fuzzy sets according to ranges
			FuzzySet1 madnegative = new FuzzySet1(0.0,0.4);
			FuzzySet1 madzero = new FuzzySet1(0.3,0.7);
			FuzzySet1 madpositive = new FuzzySet1(0.6,1.0);
			FuzzySet1 priceverylow = new FuzzySet1(0.0,0.3);
			FuzzySet1 pricelow = new FuzzySet1(0.25,0.5);
			FuzzySet1 pricemedium = new FuzzySet1(0.4,0.6);
			FuzzySet1 pricehigh = new FuzzySet1(0.5,0.75);
			FuzzySet1 priceveryhigh = new FuzzySet1(0.7,1.0);
			FuzzySet1 SellMany = new FuzzySet1(0.0,0.4);
			FuzzySet1 SellFew = new FuzzySet1(0.3,0.5);
			FuzzySet1 DoNotTrade = new FuzzySet1(0.4,0.6);
			FuzzySet1 BuyFew = new FuzzySet1(0.5,0.7);
			FuzzySet1 BuyMany = new FuzzySet1(0.6,1.0);
			double[] weights = {0,0,0,0,0,0,0,0,0,0,0};
			//Normalizing price values in the range of 0-1.
			double price_value = normalizeprice(price(i));
			double Mad_value = normalizemad(mad(i));
			currentpriceofshare = price(i);
			
			//Setting rules..
			//The respective fuzzy sets are cut at the minimum point of their antecedants using the condition of anding
			//So the followng lines translate to (if(mad is positive) and (price is low)) Then  BuyMany
			BuyMany.CutAt(Math.min(madpositive.Membership(Mad_value),priceverylow.Membership(price_value)));
			//(if(mad is positive) and (price is verylow)) Then  BuyFew
			BuyFew.CutAt(Math.min(madpositive.Membership(Mad_value),pricelow.Membership(price_value)));
			//(if(mad is positive) and (price is medium)) Then  DNT
			DoNotTrade.CutAt(Math.min(madpositive.Membership(Mad_value),pricemedium.Membership(price_value)));
			//(if(mad is positive) and (price is high)) Then  DNT
			DoNotTrade.CutAt(Math.min(madpositive.Membership(Mad_value),pricehigh.Membership(price_value)));
			//(if(mad is positive) and (price is very high)) Then  DNT
			DoNotTrade.CutAt(Math.min(madpositive.Membership(Mad_value),priceveryhigh.Membership(price_value)));
			//(if(mad is zero) and (price is very low)) Then  BuyMany
			BuyMany.CutAt(Math.min(madzero.Membership(Mad_value),priceverylow.Membership(price_value)));
			//(if(mad is zero) and (price is low)) Then  BuyFew
			BuyFew.CutAt(Math.min(madzero.Membership(Mad_value),pricelow.Membership(price_value)));
			//(if(mad is zero) and (price is medium)) Then  DNT
			DoNotTrade.CutAt(Math.min(madzero.Membership(Mad_value),pricemedium.Membership(price_value)));
			//(if(mad is zero) and (price is high)) Then  SellFew
			SellFew.CutAt(Math.min(madzero.Membership(Mad_value),pricehigh.Membership(price_value)));
			//(if(mad is zero) and (price is veryhigh)) Then  SellMany
			SellMany.CutAt(Math.min(madzero.Membership(Mad_value),priceveryhigh.Membership(price_value)));
			//(if(mad is negative) and (price is very low)) Then  BuyMany
			BuyMany.CutAt(Math.min(madnegative.Membership(Mad_value),priceverylow.Membership(price_value)));
			//(if(mad is negative) and (price is low)) Then  BuyFew
			BuyFew.CutAt(Math.min(madnegative.Membership(Mad_value),pricelow.Membership(price_value)));
			//(if(mad is negative) and (price is medium)) Then  SellFew
			SellFew.CutAt(Math.min(madnegative.Membership(Mad_value),pricemedium.Membership(price_value)));
			//(if(mad is negative) and (price is high)) Then  SellMany
			SellMany.CutAt(Math.min(madnegative.Membership(Mad_value),pricehigh.Membership(price_value)));
			//(if(mad is negative) and (price is very high)) Then  SellMany
			SellMany.CutAt(Math.min(madnegative.Membership(Mad_value),priceveryhigh.Membership(price_value)));
			
			//Initializing the weights of the array according to the respective fuzzy sets
			for(int j=0;j<11;j++)
			{
				weights[j] = weights[j] + SellMany.Membership((double) j/10);
				weights[j] = weights[j] + SellFew.Membership((double) j/10);
				weights[j] = weights[j] + DoNotTrade.Membership((double) j/10);
				weights[j] = weights[j] + BuyFew.Membership((double) j/10);
				weights[j] = weights[j] + BuyMany.Membership((double) j/10);
			}
			
			double cog = (weights[1] * 0.1) + (weights[2] * 0.2) + (weights[3] * 0.3) + (weights[4] * 0.4) + (weights[5] * 0.5) +
							(weights[6] *0.6) + (weights[7] * 0.7) + (weights[8] * 0.8) + (weights[9] * 0.9) + (weights[10] * 1.0);
			cog = cog/(weights[0] + weights[1] + weights[2] + weights[3] + weights[4] + weights[5] + weights[6] + weights[7] + weights[8] + weights[9] + weights[10]);
			
			//Actual trading of shares 
			trade(defuzzifyshares(cog));
			
			currentamount = (totalmoney + (currentpriceofshare*numberofshares));
			
			dailyprofit =  currentamount - lastamount;
			
			totalassets = (totalmoney + (currentpriceofshare*numberofshares));
		    
			lastamount = currentamount;
		    
			totalprofit = ((totalmoney + (currentpriceofshare*numberofshares)) - 10000);
		    
			out.println("			Day"+i+"		");
			String s="Suggestion:" + defuzzifyshares(cog) + " Price Of Share: " + currentpriceofshare + " Money: " + totalmoney + " Number of Shares: " + numberofshares + " Total Asset: " + (totalmoney + (currentpriceofshare*numberofshares));
		    out.println(s);
		    
		}
		System.out.println(totalassets);
		out.close();
	}

}

