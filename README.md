# JavaFX-artclod

This is a repo where I'll be adding code related to posts on my blog "Any Relation to Code Living or Dead" (http://kristiankime.blogspot.com/). This repository will primarily contain code related to JavaFX, but will undoubtedly contain other related support code as well.

## Features

Currently the major feature(s) of the repository are:
1. Ref and Swap Observables
2. Property Text Fields
3. Enhanced Observable List 

### 1) Ref and Swap Observables

Ref and Swap objects are essentially pointers to other objects. They provide the ability to have one Object refer to a "fixed" object but have that object switch around. The Observable versions of these objects handle the un/binding automatically. So if you bound a TextField's text property to a PropertySwap<String> you can swap out which Property<String> the Swap points to and un/rebinding will be done automatically.  

```java
Property<String> propertyOne = new new SimpleStringProperty("one");
Property<String> propertyTwo = new new SimpleStringProperty("two");
PropertySwap<String> propertySwap = SimplePropertySwap.create(propertyOne);

TextField textField = new TextField();
// Here the textField is bound to propertyOne
textField.textProperty().bindBidirectional(propertySwap) 

// Now textField is automatically unbound from propertyOne and bound to propertyTwo
propertySwap.swap(propertyTwo)
	
```

While this in itself can be more clear/save some time the real savings comes with the BeanSwap/Ref Objects. Essentially properties can be "pulled" off the Swap bean and then if the underlying bean changes all the "pulled" properties automatically update as well. The BeanSwapExample Object under src/example/java has a short illustration of this.

### 2) Property Text Field

The short version is a PropertyTextField is a TextField that can be bound to non String Properties easily.

In a little more detail it extends the TextField Object and adds a valueProperty. This property can be of an arbitrary type and automatically updates the textProperty of the TextField. Thus you can create a PropertyTextField with an Integer valueProperty and bind Property<Integer>(s) to it.

### 3) Enhanced Observable Lists

There are a few ObservableList related objects included in the library.

1. BeanObservableList 
	* An ObservableList that fires events when properties of beans in the list change (as well as if the list of items changes)  
2. ObservableListMirror
	* An ObservableList what mirrors another list but the elements are transformed.

### History
2013-07-13 Updated to latest JavaFX gradle plugin so there should no longer be any need to remove "src/main" from the build path after eclipse regens it.

2012-12-05 General updates and switch to gradle plugin for javafx handling (http://speling.shemnon.com/blog/2012/11/07/javafx-gradle-plugin-0.0.0-released/)

2012-05-22 This code is still a work in progress and should not taken as anything other then an illustration of how one might implement a presentation model support library. 
