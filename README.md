# GIS
Implementation of a system that indexes and provides search features for a file of GIS Records

Read me:

My hashTable.java holds a data type of nameEntry.java. 
This is used in my NameIndex class, where it holds an implementation of my hashTable<nameEntry>. 

My PrQuadTree holds a data type of Point.java. 
This is used in my CoordinateIndex class, where it holds an implementation of my PrQuadTree<Point>

I used a LinkedList<String> to implement my BufferPool. 

The Linked List will store elements that store the offset and the GIS record line of the last called record.
This is used in my BufferIndex class, where it holds an implementation of my BufferPool<T>

The way I designed my GISRecord object is I passed in a string as the parameter and the offset at where I found it at, and I split the string based of the | . Then I had getters to get the things I needed from the GIS string such as FeatureClass and State.

My NameIndex handles my operations with hashTable. 
It instantiates a hashTable, and has methods for inserts into the hashTable and whatIs method to find what the element is.

My coordinate index handles my operations with prQuadTree. 
It instantiates a prQuadTree, and has methods that deal with the prQuadInteral such as whatIsAt, whatIsIn, show, convertLat, convertLong, setWorld, and getters for the xMin, xMax, yMin, yMax.

How the code runs is it takes three arguments:
    java GIS database file name, command script file name, log file name
