## Language TODOs

- Rudimentary parser for system f testing?
- modules / being able to run a Full Program
- Higher kinded types / type constructors? 
  - sum types here too?
- Lock down rank restrictions
- Very basic type inference? 
    - figure out *when* type app,, actually should happen,,
- Existential types
- product types/tuples? as primitives? maybe?
- type aliases?

----

Want to get bi-directional typing set-up.
- Convert FPrimitiveType to a more general constructed types type, allow to include user made types
- clarify variables vs symbols
- clarify environments (stronger separation between statics and dynamics)
- clarify Program vs Module vs Expression
- clarify how symbols relate to a program context

----

What is a program:
- execution environment + expression(s but 1 entry point?) + dependencies imported
So what's an execution environment?
- tells what/when to run. handles type checking. view of deps?

Is execution environment different at comp and runtime?
- Probably? compile time only needs to know **types** of expressions referenced by symbols and the single
  top level expression it's type checking. Runtime needs to know expression values to sub in for variables.
- runtime won't need to bubble up substitutions like comp time might (under a HM style algo it'd need to bubble them up?)

Is there a difference between overall environment (ie info on other functions/deps) vs context that's updated
as the program moves down the tree? 
- overall env won't be really updated ever, or rather those values won't be
- so, like, could probably  