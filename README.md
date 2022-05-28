
# 1. depencdencies:
- Java min 11
- maven min 3.x
# 2. How to run
## Step1 Run Unit tests[optional]
<code>mvn test</code>
<br> Run all unit tests with a default data.txt as resources data file

## Step2 package the application
<code>mvn package</code>
<br>a file named instanaApp.jar will be generated in the root path

## Step3 Prepare data file
create a data file, which including the Edges. I have already created one for you. feel free to replace with yours
<br>i.e: in data.txt
  > AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7

and then put the file into root path

you can also put the file anywhere you want, but supply the full file name as a parameter
## Step4 Run the application
<code>java -jar instanaApp.jar [datafile] </code> 
<br>i.e: <code>java -jar instanaApp.jar ./data.txt</code>
<code>java -jar instanaApp.jar /Users/simon/src/ibm_interview_Instana_java_sdk/data.txt</code>
# 3. Concepts
- node
- edge
- vertex
## 3.1 node
- **Class**: There is no class to present a node.
- **Description**: A point in the graph. source node means the start point in a question, and destination point means the target point in a question.
  - always present a node with a Character, not a String

## 3.2 edge
- **Class**: com.instana.graph.Edge 
- **Description**: A line segment, include 2 nodes (from, to), and a distance 
<br>**i.e: AB5**
  - A is from node
  - B is to node
  - 5 is distance between them. 


## 3.3 vertex
- **Class**: com.instana.graph.Vertex 
- **Description**: From a source node, presents the distance and path from source node to destination node.
  - destination: the point node we're talking about, which is the destination node
  - previous: from a source node, how to reach the destination node? previous is the last point before reach the destination node.
  
From A point, to reach C point, across B, two vertices can present the solution:  AB5 and CB4

# 4. Architecture
- Service layer
- Mode layer
- Application layer
- Exception
- common
## 4.1 Service layer
From business logic review, presents stories around **tracing** and **service**.
## 4.2 Mode layer
From implement view, we use a directed graph to implement all features. it doesn't accord any concepts of tracing and service, but graphic concepts. there are 3 parts in this layer.
- interface: com.instana.graph.IGraph presents the interface of any graph.
- implement: com.instana.Digraph, we use a directed graph to implement the IGraph interface.
- AdjacencyList: com.instana.AdjacencyList, we use adjacency list to present a graph. For more information, to see: https://www.geeksforgeeks.org/graph-and-its-representations/
## 4.3 Application layer
InstanaApplication.java presents the business logic in the main function
## 4.4 Exception
create some customized exceptions to present special cases.
- InputFormatException: means input is not correct
- NotFoundException: means something is not exist. especially a node is not exist, i.e: in the project, if you input any node 'Z', or ' ', this exception appear.
- GraphException: some exception when we calculate in the graph, by default, it shouldn't appear, it means graphic algorithm error when it threw
- TraceNotFoundException: any exist in Service layer, means the property trace fit for the criteria doesn't exist. 
## 4.5 common
Tools.java implements some common functions, these functions are due to serialize/deserialize or read file. i.e: covert data between string and objects
# 5. Algorithm
## 5.1 Storage
Adjacency List, generally Adjacency List is present as an array and collection of lists. but to search a node quickly, we don't use array and list, but:
- Use **HashMaps<Character, AdjacencyList> to replace array
- Use **Map<Character, Integer>** in adjacencyList, to replace list
For more information about Adjacency List, to see: 
- https://www.youtube.com/watch?v=eQA-m22wjTQ
- https://www.geeksforgeeks.org/graph-and-its-representations/
## 5.2 Deep First Search
For question 6 and 7, we solve it with DFS. For more information, to see: https://www.youtube.com/watch?v=7fujbpJ0LB4
## 5.3 Dijkstra’s algorithm
To find the shortest path between 2 nodes, we use Dijkstra’s algorithm. it based on:
- priority queue
- Breadth First Search https://www.youtube.com/watch?v=oDqjPvD54Ss

For more information about Dijkstra’s algorithm, to see https://www.youtube.com/watch?v=EFg3u_E6eHU