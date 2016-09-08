# The HiRiQ Problem
In short: Given a random board state and 2 possible move types (BBW to WWB and vise-versa), find the path of moves required to get the solved configuration (where every peg is black except for the middle one).

For more info:  access this link: http://crypto.cs.mcgill.ca/~crepeau/COMP250/HW4.pdf

# My Solution

The solution is based on the A* path-finding algorithm. As it traverses the imaginary tree of moves, it places child nodes in a priority queue based on "how close" it is to the solved state. This value is determined from the function f(x)* = h(x) + g(x), where h(x) is the similarity between the current state and the solved state, and g(x) is the number of moves from the current state to the original state. The more I weigh h(x) (the more I make h(x) more significant), the faster the algorithm solves the problem, but the less optimal the solution.

*Note: As we progress through the artificial tree, we expect to see a decrease in the values of f(x).
