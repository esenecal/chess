# Notes

Project notes for CS 240

## Chess Project Structure

`ChessPosition`: this class represents a position on a chess board, from 1-8. It is used for all representations on the
chess board--for instance, when a piece moves, its movement will be represented by a change in positions, from one to
another.

`ChessMove`: this class represents the movement of pieces on the chessboard. Each object consists of two ChessPositions,
one which signifies the start position, and another the end. It also contains any promotional pieces involved in the
movement.

`ChessPiece`: this class represents a single piece, which has a team color (black/white) and a type (bishop, king, etc.
represented by an enum). It also contains code for calculating what eligible moves for a piece are. Note that a piece
does NOT track its own position. Moves and positions are handled separately.

`ChessBoard`: This contains the board. It has a 2 dimensional array that represents the board, with 8 indices for rows
and columns. This has method to add pieces at specific ChessPositions, get the piece at a specific ChessPosition, and
reset the board.

## OOP Principles

In OOP, we want to solve a problem and abstract things. This is so that we hide any uneeded details and make our code open to modification and addition.

**Encapsulation**: placing fields and methods in classes. Restricting access to some components using `private`, with
getter/setter methods to manipulate fields. You hide details that don't matter.

**Abstraction**: hiding complex internal details from the user, showing them essential functionality that they need. A 
focus on what something does instead of how. For car, for instance, you do not need to know how the calipers or brakes work. You just need to know how to use the gas, break, and wheel. This is providing the right details that matter.

**Inheritance**: allowing a class to adopt properties of another, creating a hierarchical, superclass to subclass 
relationship. Subclasses gain non-private fields/methods from superclasses, and can expand with more features or 
override features from its superclass. This allows you to add code.

**Polymorphism**: allowing "objects of different types to be treated as objects of a common superclass." A uniform interface allows for consistent interactions with these objects.
- Static Polymorphism (Method Overloading): multiple methods in the same class having the same name, but are defined with different parameters
- Dynamic Polymorphism (Method Overriding): subclass provides a specific implementation of a method defined by a superclass or an interface.

## [Architectural Patterns](https://masteryls.com/course/7b46ce1a-2b79-4455-b706-aea79f1b51ac/topic/4419212e-ba5e-4298-86db-3b3374243973)

Polymorphism Again: By creating a common interface, each object under that interface can be treated the same.

Single Responsibility Rule (SRP): each class has one responsibility, and only one reason to change.

### Decoupling Rules from Entities

We want to avoid creating "fat entities". We do not want to couple a class's identity with, say, our game rules. 