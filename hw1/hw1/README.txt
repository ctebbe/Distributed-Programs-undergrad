I had an ant build.xml set up before going over makefile in lab so I used that to compile. Running 'ant' in the build.xml directory will compile the /src directory and place a runnable jar file in the /dist directory. Alternatively, you can run the registry or messaging nodes from the build.xml file as well. Running 'ant clean' will wipe out the compiled files and jar.

I used an IP-based hashing scheme for the messaging nodes so they must each be run on a separate machine, however, the registry and a messaging node can be run from the same machine.

Using the 'print-shortest-path' command on the messaging node prints the order of edges to traverse and their weights on it's shortest path but the order of the vertices on the edge may be backwards.

Lastly, I found when starting the registry and immediately starting the nodes after via a bash script sometimes caused a node to try to connect to the registry before it was completely set up. I solved this by adding a small wait in the bash script after starting the registry to ensure it was set up before any nodes attempt to connect.
