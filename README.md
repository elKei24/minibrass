![MiniBrass Logo -- Specifying goals the declarative way](https://raw.githubusercontent.com/isse-augsburg/minibrass/master/docs/img/logonav.png)

MiniBrass is a language and library that adds support for soft constraints to MiniZinc/MiniSearch to model weak/soft constraints, wishes and preferences systematically. 
MiniBrass pays tribute to the tradition of naming NICTA's G12 software after elements in the 12th group of the periodic table. Brass is firstly an alloy that contains zinc and, according to the [German Wikipedia](https://de.wikipedia.org/wiki/Messing), 
> Cold forming is possible with brass alloys containing up to 37% zinc. At higher zinc rates, only warm forming at temperatures > 600 °C is possible.

More specifically, we aim to provide
- Convenient expression of various types of soft constraint problems
- Automatic translation to weighted CSP
- Generic search heuristics
- Consistency checks of the specified relationships
- Integration of soft global constraints

To see a "Hello, World"-example, consider the following hard constraint model in MiniZinc
```c++
% the basic, "classic" CSP 
set of int: NURSES = 1..3;
int: day = 1; int: night = 2; int:off = 3;
set of int: SHIFTS = {day,night,off};

array[NURSES] of var SHIFTS: n;

% additional hard constraints would be here 

solve 
:: pvsSearchHeuristic
satisfy;

output ["n = \(n)"] ++ 
       [ "\nValuations:  topLevelObjective = \(topLevelObjective)\n"];
```

that is accompanied by the following MiniBrass model
```c++
type ConstraintPreferences = PVSType<bool, set of 1..nScs> = 
  params { 
    array[int, 1..2] of 1..nScs: crEdges;
    bool: useSPD;
  } in 
  instantiates with "soft_constraints/mbr_types/cr_type.mzn" {
    times -> union_violateds;
    is_worse -> is_worse_constraint_pref;
    top -> {};
};
    

PVS: cr1 = new ConstraintPreferences("cr1") {
   soft-constraint c1: 'sum(i in NURSES)(bool2int(n[i] = night)) = 2';
   soft-constraint c2: 'n[2] in {day,off}';
   soft-constraint c3: 'n[3] = off';
   
   crEdges : '[| mbr.c2, mbr.c1 | mbr.c3, mbr.c1 |]';
   useSPD: 'true' ;
}; 

solve cr1;
```

Execute this pair using
```
minibrass nurseHelloWorld.mbr nurseHelloWorld.mzn
```
as shown in https://github.com/isse-augsburg/minibrass/tree/master/example-problems/nurse-example 

For a *user-centered* perspective, please refer to our [main page](http://isse-augsburg.github.io/minibrass/).

### Dev Setup

MiniBrass is mirrored on gitlab to access their CI/CD features. To setup your local repository appropriately,
please consult [the wiki setup page](https://github.com/isse-augsburg/minibrass/wiki/Setting-up-a-new-git-repo-with-two-remotes).

### Tech

MiniBrass is based on these open source projects:

* [MiniZinc] - Of course, its underlying modeling language!
* [MiniSearch] - an extension of MiniZinc to support more flexible search strategies

### Project overview
* **source-code/java** provides the MiniBrass compiler to model using partial valuation structures (PVS)
* **source-code/minizinc** contains the MiniBrass library containing predicates and functions in MiniZinc
* **source-code/kiv** contains PVS-related proofs in the KIV theorem prover
* **example-problems** shows problems referred to in the documentation

### Test cases

To be able to run the test cases from Eclipse, make sure to start it in interactive mode (or link the solvers/minizinc binaries in /usr/bin or some other binaries directory)
```
bash -ic ./eclipse
```


   [MiniZinc]: <http://www.minizinc.org/>
   [MiniSearch]: <http://www.minizinc.org/minisearch/>
 

