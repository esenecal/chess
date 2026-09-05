# Notes

Project notes for CS 240

## OOP Principles

**Encapsulation**: placing fields and methods in classes. Restricting access to some components using `private`, with
getter/setter methods to manipulate fields.

**Abstraction**: hiding complex internal details from the user, showing them essential functionality that they need. A 
focus on what something does instead of how. 

**Inheritance**: allowing a class to adopt properties of another, creating a hierarchical, superclass to subclass 
relationship. Subclasses gain non-private fields/methods from superclasses, and can expand with more features or 
override features from its superclass.

**Polymorphism**: allowing "objects of different types to be treated as objects of a common superclass." A uniform interface allows for consistent interactions with these objects.
- Static Polymorphism (Method Overloading): multiple methods in the same class having the same name, but are defined with different parameters
- Dynamic Polymorphism (Method Overriding): subclass provides a specific implementation of a method defined by a superclass or an interface.

## [Architectural Patterns](https://masteryls.com/course/7b46ce1a-2b79-4455-b706-aea79f1b51ac/topic/4419212e-ba5e-4298-86db-3b3374243973)

Polymorphism Again: By creating a common interface, each object under that interface can be treated the same.

Single Responsibility Rule (SRP): each class has one responsibility, and only one reason to change.

### Decoupling Rules from Entities

We want to avoid creating "fat entities". We do not want to couple a class's identity with, say, our game rules. 