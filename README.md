# KD-Tree Starbucks Locator

This repository contains my implementation of a 2D KD-tree used to efficiently search for the nearest Starbucks location based on geographic coordinates (latitude and longitude).

This project was developed as part of a university assignment. The abstract class `Starbucks` and the `Locations` class were provided by the instructor. The KD-tree logic in `StudentStarbucks.java` is entirely my own work.

## 🌍 Features

- Builds a 2D KD-tree from a list of Starbucks locations
- Efficient nearest-neighbor lookup using recursive spatial pruning
- Avoids duplicate locations within a tight coordinate tolerance
- Designed to work with a framework provided in class

## 🧠 What I Implemented

- Full KD-tree construction using recursive median splits
- Nearest-neighbor search with axis-aligned pruning
- Duplicate location filtering using a precision threshold
- Recursive logic to select optimal branches and backtrack intelligently

## 🧪 Sample Method Signatures

```java
public void build(Locations[] allLocations);
public Locations getNearest(double lng, double lat);
```

## 🚫 Limitations

- Requires the `Starbucks` abstract class and `Locations` object from the course framework to compile.
- Not runnable as-is without the provided scaffolding.

## ✅ Usage (Conceptual)

While the project depends on external classes, the logic inside `StudentStarbucks.java` is cleanly structured and can be adapted into standalone applications with minor changes.

## 📌 Credits

- KD-tree logic by **Shane Paton**
- Base classes and testing framework provided by instructor
