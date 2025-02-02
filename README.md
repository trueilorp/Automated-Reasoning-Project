Input Handling
The program reads input from a text file (input.txt) and processes it based on specific constraints and rules.

Input Constraints
No Uppercase Letters
The input formula must not contain uppercase letters, except for predefined logical symbols.

Inequality Symbol (#)
The symbol # represents not equal (≠).

Restricted Variables (u, v)
The variables u and v should not appear in the input formula, as they are reserved for preprocessing formulas in the theory of lists.

Restricted Function Name (ff)
The function name ff should not be used, as it is reserved for preprocessing formulas related to the theory of arrays.

Disjunctive Normal Form (DNF) Transformation
If an input formula ends with "--- T0-TRASFORM-DNF", the program transforms it into Disjunctive Normal Form (DNF). If this marker is absent, DNF transformation is skipped.

Parenthesis Usage
Round Brackets () → Used exclusively for function or predicate arguments.
Square Brackets [] → Used to define the scope of logical operators such as &, |, and ->.
Universal and Existential Quantifiers
Universal Quantification → Represented using FORALL(x).
Existential Quantification → Represented using EXISTS(y).


How to run the program
Input: take a formula from the file inputList.txt and paste it into the file input.txt.
The type:
> javac Main.java
> java Main 