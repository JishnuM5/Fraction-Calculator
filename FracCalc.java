//The import statements provide more functionality and methods to use
import java.math.BigInteger;
import java.util.Scanner;
import java.util.Arrays;
public class FracCalc {
    //The main method takes and stores input and prints answer
    public static void main(String[] args){
        //Creating Scanner instance, welcoming user
        Scanner console = new Scanner(System.in);
        System.out.print("Welcome to FracCalc!");
        String expression = " ";

        //Loop running while the expression is not (or DOS Error), bidding user farewell
        while (!expression.equalsIgnoreCase("quit")) {
            System.out.println(expression);
            System.out.print("Enter: ");
            if (console.hasNextLine()) {
                expression = processInput(console.nextLine().trim());
            } else {
                //A DOS/UNIX Error is an happens when entering Ctrl Z or X
                System.out.println("\nError: DOS/UNIX command for end of input");
                expression = "quit";
            }
        }
        System.out.println("Good Bye!");
    }

    //This method processes the input (quit, help, calculate)
    //Either 'fractionizes' inputs and sends for calculation or returns a command
    public static String processInput(String input) {
        if (input.equalsIgnoreCase("quit")) {
            //If the input is "quit", it returns that back, stopping the while loop
            return input;
        } else if (input.equalsIgnoreCase("help")) {
            //If the input is "help", then prints/returns a cool help command
            System.out.println("**************************************************");
            System.out.println("Hello! This is FracCalc, a fraction calculator");
            System.out.println("FracCalc can calculate any number of values and digits");
            System.out.print  ("Follow this format (with spaces): ");
            System.out.println(" <num1> <+,-,*,/,^> <num2> <+,-,*,/,^> <num3>...");
            System.out.println("Numbers can be Integers         : <int>");
            System.out.println("Proper or Improper Fractions    : <numer>/<denom>");
            System.out.println("Or Mixed Numbers                : <int>_<numer>/<denom>");
            System.out.println("You may use parentheses around values, also spaced");
            System.out.println("(Exponents must be a whole numbers and not BigIntegers)");
            System.out.println("(Calculations with BigIntegers in the denominator take a while)");
            return            ("**************************************************");
        } else {
            //Else, calculate with input (or send back an error in the process)
            //Creating a scanner, variables for array length, final answer and a flag
            Scanner scan = new Scanner(input);
            int wordNum = 1;
            boolean doubleSpace = false;
            String finalAns = "";
            //If the length of the input is 0, return a blank input error
            if (input.length() == 0) {
                return "Error: blank input";
            }

            //For loop traversing input as a char array, counting # of tokens
            for (char c : input.toCharArray()) {
                if (c == ' ' && !doubleSpace) {
                    //If char is a space and the previous char wasn't, +1 to array length
                    wordNum++;
                    doubleSpace = true;
                } else if (!(c == ' ')) {
                    //If char is not a space, no double space
                    doubleSpace = false;
                }
            }
            //Declaring array to store input, +1 to store extra space (used later on)
            String[] inputVal = new String[wordNum+1];
            for(int i = 0; i < wordNum; i++) {
                inputVal[i] = scan.next();
            }
            inputVal[inputVal.length-1] = " ";

            //Checking for bounds errors or calling method to find answer and returning it
            String o = inputVal[0];
            boolean tk1 = o.equals("+")||o.equals("-")||o.equals("*")||o.equals("/")||o.equals("^");
            if (wordNum > 2 && !tk1) {
                //Checking if input is long enough, first token isn't an operator
                finalAns = orderOfOp(inputVal);
            } else {
                //Triggers invalid expression error
                inputVal[inputVal.length-1] = "";
            }
            //If there is already an error, then no change --> !contains("Error")
            if (inputVal[inputVal.length-1].equals(" ") && !finalAns.contains("Error")) {
                //If inputVal array hasn't changed (extra space still at end), error
                finalAns = "Error: no valid operator found";
            } else if (!inputVal[1].equals(" ") && !finalAns.contains("Error")) {
                //If calculations didn't complete (extra space isn't second element), error
                finalAns = "Error: invalid expression";
                if (inputVal[0].contains("inside parentheses")) {
                    //If error inside parentheses, add to error message
                    finalAns += " inside parentheses";
                }
            }
            return input + " = " + finalAns;
        }
    }

    //This method sends parts to be calculated based on Order of Operations
    public static String orderOfOp(String[] inputVal) {
        //Various flags declared
        boolean hasOpr = true, parenPair = true, paren, pow, mulDiv;

        //Loop that runs while there isn't error and there is an operator
        while (!inputVal[0].contains("Error") && hasOpr) {
            int i = 0, parenNum = 0;
            hasOpr = pow = paren = mulDiv = false;
            for (String sym : inputVal) {
                //Go through array once to check operators and parentheses
                if (sym.equals("(")) {
                    hasOpr = paren = pow = mulDiv = true;
                    parenPair = !parenPair;
                } else if (sym.equals("^")) {
                    hasOpr = pow = mulDiv = true;
                } else if (sym.equals("*") || sym.equals("/")) {
                    hasOpr = mulDiv = true;
                } else if (sym.equals(")")) {
                    //Every time parentheses found, flag switches, # of parentheses +1
                    parenPair = !parenPair;
                    parenNum++;
                }
            }

            if (!parenPair) {
                //If all parentheses aren't paired, error and for loop won't run
                inputVal[0] = "Error: missing parenthesis";
                i = inputVal.length;
            }

            for (i = i; i < inputVal.length; i++) {
                //Go through array a second time to call calculate
                if (inputVal[i].equals("(")) {
                    //More declaration: move array buy how much, closed parentheses
                    int moveArr = 0, closedParen = inputVal.length;
                    String prev = "";

                    //Searching for innermost parentheses, while parentheses still there
                    while (parenNum > 0) {
                        closedParen--;
                        if (inputVal[closedParen].equals(")")) {
                            //if parentheses found, # of paren -1
                            parenNum--;
                        }
                    }
                    //Checking if any opening parentheses up to point of closed one
                    for (int j = i; j < closedParen; j++) {
                        if (inputVal[j].equals("(")) {
                            //If found, i set to index of open parentheses
                            i = j;
                        }
                    }
                    if (closedParen - i < 4) {
                        //If there are less than 3 elements between parentheses, error
                        return "Error: invalid input inside parenthesis";
                    }
                    moveArr = closedParen-i;

                    //Calculations inside parentheses done as a another 'input'
                    prev = orderOfOp(Arrays.copyOfRange(inputVal, i+1, closedParen));
                    if (prev.contains(" ") && !prev.contains("Error")) {
                        //If no error and calculations completed, answer put into original array
                        prev = prev.substring(0, prev.length()-1);
                        inputVal = orderOfOp2(inputVal, i, moveArr, prev);
                    } else {
                        //Else, error sent back
                        return prev + " inside parentheses";
                    }
                    i = inputVal.length;
                } else if (inputVal[i].equals("^") && !paren) { 
                    inputVal = orderOfOp2(inputVal, i-1, 2, "none");
                } else if ((inputVal[i].equals("*") || inputVal[i].equals("/")) && !pow) {
                    inputVal = orderOfOp2(inputVal, i-1, 2, "none");
                } else if ((inputVal[i].equals("+") || inputVal[i].equals("-")) && !mulDiv) {
                    inputVal = orderOfOp2(inputVal, i-1, 2, "none");
                    hasOpr = true;
                }
            }
        }

        if (inputVal[0].contains("Error")) {
            //If there is error, just send that back
            return inputVal[0];
        }
        return inputVal[0] + inputVal[1];
        //Otherwise, send it along with 2nd element, to check for errors in prev
        //Doesn't affect because it just adds an extra space in  final answer
    }

    //This method processes a single operation. calculates it, and deals with errors
    public static String[] orderOfOp2(String[] inputVal, int i, int moveArr, String prev) {
        //Declaration: arrays to store errors and/or fraction values
        int[] numVal = {1, 1, 1, 1};
        BigInteger[] bigIntVal = new BigInteger[4];
        //Boolean array used so that it is by reference, same across methods
        boolean[] bigInt = new boolean[1];
        String ans = "", opr = "";

        if (prev.equals("none")) {
            //If there is no previous answer, calculate
            //Numbers broken down to improper and then numerator and denominator values
            String num1 = toFrac(inputVal[i], bigInt);
            opr = inputVal[i+1];
            String num2 = toFrac(inputVal[i+2], bigInt);
            if (bigInt[0]) {
                //If toFrac found a Big Integer, calculations switch to there
                //numVal still used but only for errors
                bigIntVal = fracVals(num1, num2, opr, numVal);
            } else {
                numVal = fracVals(num1, num2, opr);
            }
            //Specific patterns in numVal means different errors
            //Array always has a 0 in a denominator so will never happen normally
            if (numVal[1] == 0 && numVal[3] == 0) {
                String[] error = {"Error: invalid expression"};
                return error;
            } else if (numVal[0] == 0 && numVal[1] == 0) {
                //If the error array was returned
                String[] error = {"Error: divide by zero"};
                return error;
            } else if (numVal[2] == 0 && numVal[3] == 0) {
                String[] error = {"Error: invalid exponent"};
                return error;
            } else if (numVal[1] == 0 && numVal[2] == 0) {
                //This pattern means a Big Integer was found
                //Too lazy to add bigInt boolean as a parameter instead
                ans = calc(fracVals(num1, num2, opr, numVal), opr, true);
            } else {
                //If no changes needed calculate as usual
                if (bigInt[0]) {
                    ans = calc(bigIntVal, opr, true);
                } else {
                    ans = calc(numVal, opr);
                }
            }
        } else {
            //If a previous answer is being inserted, then no calculations
            ans = prev;
            if (!ans.contains("Error")) {
                toFrac(prev, bigInt);
                //Dead call but does check for Big Integer in fraction
                if (bigInt[0]) {
                    //not calculating here, just using method to reduce
                    ans = calc(fracVals(ans, "0/1", opr, numVal), opr, true);
                } else {
                    ans = calc(fracVals(ans, "0/1", opr), opr);
                }
            }
        }

        //Replacing old expressions with answer, shifting rest of array
        for (int j = i+1; j < inputVal.length-moveArr; j++) {
            //All up to moveArr shifted to left as original values
            inputVal[j] = inputVal[j+moveArr];
        }
        for (int j = inputVal.length-moveArr; j < inputVal.length; j++) {
            //Non-breaking space used to fill rest of array, ASCII 255
            inputVal[j] = " ";
        }
        inputVal[i] = ans;

        //Printing out steps, returning array if no errors returned
        if (Arrays.toString(inputVal).contains("  ")) {
            //If not a calculation inside parentheses (no extra space at end), print array
            for (String s : inputVal) {
                //Cleaner look than Arrays.toString()
                System.out.print(s + " ");
            }
            System.out.println();
        }
        return inputVal;
    }

    //This method converts the numbers to proper/improper fractions
    public static String toFrac(String num, boolean[] bigInt) {
        if (num.contains("_") && num.contains("/")) {
            //If the number is mixed, the parts of the number need to be separated
            //Adding whitespace so input can be scanned, declaring scanner
            num = num.replace("_", " _ ");
            num = num.replace("/", " / ");
            Scanner scan = new Scanner(num);

            if (mixedChecker(scan, bigInt)) {
                //If the number is valid and whether or not it is a Big Integer
                if (bigInt[0]) {
                    BigInteger denm, wholeNum, numr;
                    denm = new BigInteger(num.substring(num.indexOf('/')+2));
                    wholeNum = new BigInteger(num.substring(0,num.indexOf('_')-1));
                    numr = new BigInteger(num.substring(num.indexOf('_')+2,num.indexOf('/')-1));
                    return (numr.add(wholeNum.multiply(denm))) + "/" + denm;
                } else {
                    //Parse to int, whole number added to original numerator to get improper
                    int denm, wholeNum, numr;
                    denm = Integer.parseInt(num.substring(num.indexOf('/')+2));
                    wholeNum = Integer.parseInt(num.substring(0,num.indexOf('_')-1));
                    numr = Integer.parseInt(num.substring(num.indexOf('_')+2,num.indexOf('/')-1));
                    return (numr + wholeNum*denm) + "/" + denm;
                }
            } else {
                //If invalid fraction, return "n/a" and no Big Integers.
                bigInt[0] = false;
                return "n/a";
            }
        } else if (num.contains("/")) {
            //If the number is a proper fraction, return original value
            num = num.replace("/", " / ");
            Scanner scan = new Scanner(num);
            num = num.replace(" / ", "/");

            if (properChecker(scan, bigInt)) {
                //Again, checking for validity and Big Integers
                return num;
            } else {
                bigInt[0] = false;
                return "n/a";
            }
        } else {
            //If the number is a whole, it is put over one
            boolean isNum = true;
            Scanner scan = new Scanner(num);

            //No point in creating a separate method for this much
            if (!scan.hasNextInt()) {
                //If not an int, check if Big Integer, or number is invalid
                if (scan.hasNextBigInteger()) {
                    bigInt[0] = true;
                } else {
                    isNum = false;   
                }
                scan.next();
            }

            if (isNum) {
                return num + "/1";
            } else {
                bigInt[0] = false;
                return "n/a";
            }
        }
    }

    //This method checks if a mixed number is an int, Big Integer, or invalid
    public static boolean mixedChecker (Scanner scan, boolean[] bigInt) {
        //Mixed numbers have 5 'tokens', 3 numbers, "_", and "/"
        for (int i = 0; i < 5; i++) {
            if (scan.hasNext()) {
                if (!scan.hasNextInt() && i%2 == 0) {
                    //If a number is not an integer, check for Big Integer or invalid
                    if (scan.hasNextBigInteger()) {
                        bigInt[0] = true;
                    } else {
                        return false;   
                    }
                    scan.next();
                } else if (i == 1) {
                    //Second token should be "_"
                    if (!scan.next().equals("_")) {
                        //2 separate ifs because i has to stop above, then check validity
                        return false;
                    }
                } else if (i == 3) {
                    //Fourth token should be "/"
                    if (!scan.next().equals("/")) {
                        return false;
                    }
                } else { 
                    scan.next();
                }
            } else {
                //If the mixed number doesn't have 5 tokens, invalid
                return false;
            }
        }

        //If more than 5 tokens, invalid, and if no errors, return true (valid)
        if (scan.hasNext()) {
            return false;
        }
        return true;
    }

    //This method checks if a proper/improper is an int, Big Integer, or invalid
    //Very similar to the above method, just different number format
    public static boolean properChecker(Scanner scan, boolean[] bigInt) {
        //Proper/Improper fractions should have 3 'tokens': 2 numbers and a "/" between
        for (int i = 0; i < 3; i++) {
            //Refer to mixedChecker for explanation of if statements
            if (scan.hasNext()) {
                if (!scan.hasNextInt() && i%2 == 0) {
                    if (scan.hasNextBigInteger()) {
                        bigInt[0] = true;
                    } else {
                        return false;  
                    }
                    scan.next();
                } else if (i == 1) {
                    //Second token should be "/"
                    if (!scan.next().equals("/")) {
                        return false;
                    }
                } else {
                    scan.next();
                }
            } else {
                return false;
            }
        }
        if (scan.hasNext()) {
            return false;
        }
        return true;
    }

    //This method gets values of fractions and checks for errors
    public static int[] fracVals(String num1, String num2, String opr) {
        //Both fractions parsed for numerator and denominator and stored in array
        int[] numVal = new int[4];
        if (!(num1.equals("n/a") || num2.equals("n/a"))) {
            numVal[0] = Integer.parseInt(num1.substring(0,num1.indexOf('/')));
            numVal[1] = Integer.parseInt(num1.substring(num1.indexOf('/')+1));
            numVal[2] = Integer.parseInt(num2.substring(0,num2.indexOf('/')));
            numVal[3] = Integer.parseInt(num2.substring(num2.indexOf('/')+1));
        }
        //The for loop removes any negatives in denominator to numerator
        //Avoids errors while calculating and looks better
        for (int i = 1; i < 4; i+= 2) {
            if(numVal[i] < 0) {
                //If denominator is less than zero, multiply top and bottom by -1
                numVal[i] *= -1;
                numVal[i-1] *= -1;
            }
        }

        //Checking if there are any errors with the numbers
        if (num1.equals("n/a") || num2.equals("n/a")) {
            //If toFrac assigned numbers as invalid, error
            int[] error = {1, 0, 1, 0};
            //Complexity of returning errors is because method returns int array
            return error;
        } else if (numVal[1] == 0 || numVal[3] == 0 || (opr.equals("/") && numVal[2] == 0)) {
            //If either denominator = 0 or dividing and and num2 = zero, error
            int[] error = {0, 0, 1, 1};
            return error;
        } else if (opr.equals("^")) {
            //If exponent does not equal a whole number, error
            if (numVal[2]%numVal[3] == 0) {
                //If numerator / denominator - while number, no error
                return numVal;
            } else {
                int[] error = {1, 1, 0, 0};
                return error;
            }
        } else if (calcIsBigInt(numVal, opr)) {
            //If a calculation will involve Big Integers, send bigInt array
            int[] bigInt = {1, 0, 0, 1};
            return bigInt;
        } else {
            //Else, reassign fractions in terms of a common multiple as denominator
            numVal[0] *= numVal[3];
            numVal[2] *= numVal[1];
            numVal[1] = numVal[3] = numVal[1]*numVal[3];
            return numVal;
        }
    }

    //This method gets values of BigInteger fractions and also checks for errors
    //Very similar to above method but using Big Integer methods
    //Since the parameters are different from int calc(), can overload
    public static BigInteger[] fracVals(String num1, String num2, String opr, int[] numVal) {
        //Don't need to parse with Big Integers, direct conversion
        //All invalid numbers go to int method so don't need to check here
        BigInteger[] bigNumVal = new BigInteger[4];
        bigNumVal[0] = new BigInteger(num1.substring(0,num1.indexOf('/')));
        bigNumVal[1] = new BigInteger(num1.substring(num1.indexOf('/')+1));
        bigNumVal[2] = new BigInteger(num2.substring(0,num2.indexOf('/')));
        bigNumVal[3] = new BigInteger(num2.substring(num2.indexOf('/')+1));
        
        for (int i = 1; i < 4; i+= 2) {
            if(bigNumVal[i].compareTo(BigInteger.ZERO) == -1) {
                //If denominator is less than zero, multiply top and bottom by -1
                bigNumVal[i].multiply(BigInteger.valueOf(-1));
                bigNumVal[i-1].multiply(BigInteger.valueOf(-1));
            }
        }
        
        boolean denm1Is0 = bigNumVal[1].compareTo(BigInteger.ZERO) == 0;
        boolean denm2Is0 = bigNumVal[3].compareTo(BigInteger.ZERO) == 0;
        boolean numr2Is0 = bigNumVal[2].compareTo(BigInteger.ZERO) == 0;
        if (denm1Is0 || denm2Is0 || (opr.equals("/") && numr2Is0)) {
            for (int i = 0; i < numVal.length; i++) {
                //Have to use awkward for loops to assign errors
                numVal[i] = ((i+1)/3);
            }
        } else if (opr.equals("^")) {
            int wholeExDenm = bigNumVal[2].mod(bigNumVal[3]).compareTo(BigInteger.ZERO);
            int intEx = bigNumVal[2].compareTo(BigInteger.valueOf(bigNumVal[2].intValue()));
            //Only new error checked for: if the exponent is not a Big Integer
            //Can crash the computer and Big Integer pow won't calculate
            if (wholeExDenm != 0 || intEx != 0) {
                for (int i = 0; i < numVal.length; i++) {
                    numVal[i] = (3/(i+2));
                }
            }
        } else {
            //Reassigning again, don't need to check for Big Integer here either
            bigNumVal[0] =  bigNumVal[0].multiply(bigNumVal[3]);
            bigNumVal[2] =  bigNumVal[2].multiply(bigNumVal[1]);
            bigNumVal[1] = bigNumVal[3] = bigNumVal[1].multiply(bigNumVal[3]);
        }
        return bigNumVal;
    }

    //This method does the actual calculations
    public static String calc(int[] numVal, String opr) {
        if (opr.equals("+")) {
            //If the sign is +, add num1 and num2
            numVal[0] += numVal[2];
        } else if (opr.equals("-")) {
            //If the sign is +, subtract num1 and num2
            numVal[0] -= numVal[2];
        } else if (opr.equals("*")) {
            //If the sign is *, multiply the numerator and denominator
            numVal[0] *= numVal[2];
            numVal[1] *= numVal[3];
        } else if (opr.equals("/")) {
            //If the sign is *, divide the numerator and denominator
            //Specifically, multiply with reciprocal of second number
            numVal[0] *= numVal[3];
            numVal[1] *= numVal[2];
        } else if (opr.equals("^")) {
            //If the sign is ^ numerator and denominator raised to the second number
            if (numVal[2]/numVal[3] < 0) {
                //Swap numerator and denominator if exponent is negative
                numVal[0] += numVal[1];
                numVal[1] = numVal[0] - numVal[1];
                numVal[0] = numVal[0] - numVal[1];
                numVal[2] = -numVal[2];
            }
            numVal[0] = (int) Math.pow(numVal[0], numVal[2]/numVal[3]);
            numVal[1] = (int) Math.pow(numVal[1], numVal[2]/numVal[3]);
        }
        
        //Simplifying answer, reduce flag
        boolean reduce = true;
        boolean negNumr = numVal[0] < 0;
        boolean negDenm = numVal[1] < 0;
        //The answer is temporarily turned positive when reducing
        if (negNumr) {
            numVal[0] *= -1;
        }
        if (negDenm) {
            numVal[1] *= -1;
        }
        while (reduce) {
            reduce = false;
            for (int i = 2; i <= Math.min(numVal[0], numVal[1]); i++) {
                //Checking if any value up to numerator/denominator is a factor of both
                if (numVal[0]%i == 0 & numVal[1]%i == 0) {
                    //If <num>%i == 0, it is a factor of <num>, dividing both by i
                    numVal[0] /= i;
                    numVal[1] /= i;
                    //Only when the fraction is reduced, the loop continues
                    reduce = true;
                }
            }
        }
        //Answer goes back to negative if it was before
        if (negNumr) {
            numVal[0] *= -1;
        }
        if (negDenm) {
            numVal[1] *= -1;
        }
        
        //Returning answer as a String in fraction form
        return numVal[0] + "/" + numVal[1];
    }

    //This method does the actual calculations with Big Integers and its methods
    //Very similar to above method, reduce as parameter (used in calcIsBigInt)
    public static String calc(BigInteger[] bigNumVal, String opr, boolean reduce) {
        //A few commonly used values declared as shorter variables for readability
        //Exact same method apart from that, refer to other calc for explanations
        BigInteger numr1 = bigNumVal[0], numr2 = bigNumVal[2], z = BigInteger.ZERO;
        //If statement doing calculations by operator
        if (opr.equals("+")) {
            bigNumVal[0] = numr1.add(numr2);
        } else if (opr.equals("-")) {
            bigNumVal[0] = numr1.subtract(numr2);
        } else if (opr.equals("*")) {
            bigNumVal[0] = numr1.multiply(numr2);
            bigNumVal[1] = bigNumVal[1].multiply(bigNumVal[3]);
        } else if (opr.equals("/")) {
            bigNumVal[0] = numr1.multiply(bigNumVal[3]);
            bigNumVal[1] = bigNumVal[1].multiply(numr2);
        } else if (opr.equals("^")) {
            if (bigNumVal[2].divide(bigNumVal[3]).compareTo(z) == -1) {
                numr1 = numr1.add(bigNumVal[1]);
                bigNumVal[1] = numr1.subtract(bigNumVal[1]);
                numr1 = numr1.subtract(bigNumVal[1]);
                numr2 = numr2.multiply(BigInteger.valueOf(-1));
            }
            bigNumVal[0] = numr1.pow(numr2.divide(bigNumVal[3]).intValue());
            bigNumVal[1] = bigNumVal[1].pow(numr2.divide(bigNumVal[3]).intValue());
        }
        
        //Simplifying the answer
        boolean negNumr = bigNumVal[0].compareTo(z) == -1;
        boolean negDenm = bigNumVal[1].compareTo(z) == -1;
        if (negNumr) {
            bigNumVal[0] = bigNumVal[0].multiply(BigInteger.valueOf(-1));
        }
        if (negDenm) {
            bigNumVal[1] = bigNumVal[1].multiply(BigInteger.valueOf(-1));
        }
        while (reduce) {
            reduce = false;
            for (BigInteger i = BigInteger.valueOf(2);
            i.compareTo(bigNumVal[0].min(bigNumVal[1])) <= 0;
            i = i.add(BigInteger.ONE)) {
                if (bigNumVal[0].mod(i).compareTo(z)==0 && bigNumVal[1].mod(i).compareTo(z)==0) {
                    bigNumVal[0] = bigNumVal[0].divide(i);
                    bigNumVal[1] = bigNumVal[1].divide(i);
                    reduce = true;
                }
            }
        }     
        if (negNumr) {
            bigNumVal[0] = bigNumVal[0].multiply(BigInteger.valueOf(-1));
        }
        if (negDenm) {
            bigNumVal[1] = bigNumVal[1].multiply(BigInteger.valueOf(-1));
        }
        
        //Returning answer as a String in fraction form
        return bigNumVal[0] + "/" + bigNumVal[1];
    }

    //This method checks whether or not a calculation will require Big Integers
    public static boolean calcIsBigInt(int[] numVal, String opr) {
        //Declaring max/min int as variables, fraction values moved to Big Integer array
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger[] big = new BigInteger[4];
        for (int i = 0; i < 4; i++) {
            big[i] = BigInteger.valueOf(numVal[i]);
        }
        
        //Multiplying numerator and denominator (done during common denominator)
        int num1Big = big[0].multiply(big[1]).compareTo(maxInt);
        int num2Big = big[2].multiply(big[3]).compareTo(maxInt);
        //Doing a calculation, no reduction to tell if Big Integer or not
        //Does mean each calculation happens twice, but no reducing --> lighter process
        String [] calcBig = calc(big, opr, false).split("/", 2);
        BigInteger calc1 = new BigInteger(calcBig[0]);
        BigInteger calc2 = new BigInteger(calcBig[1]);
        int calc1Big = calc1.compareTo(maxInt), calc2Big = calc2.compareTo(maxInt);
        
        //All values compared with Integer.MAX_VALUE
        if (num1Big == 1 || num2Big == 1 || calc1Big == 1 || calc2Big == 1) {
            //If numbers are larger than max int, Big Integer
            return true;
        } else {
            //If numbers are not larger, compare to min int and return result
            num1Big = big[0].multiply(big[1]).compareTo(minInt);
            num2Big = big[2].multiply(big[3]).compareTo(minInt);
            calc1Big = calc1.compareTo(minInt);
            calc2Big = calc2.compareTo(minInt);
            return (num1Big == -1 || num2Big == -1 || calc1Big == -1 || calc2Big == -1);
        }
    }
}
