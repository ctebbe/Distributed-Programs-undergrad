#!/bin/bash

HOST=$(hostname)
PORT="8080" # default registry port

#ant clean

# registry start-up
gnome-terminal -x bash -c "ant -Darg0=${PORT} registry"

# messaging node start-up
for i in `cat machines_10`
do
    echo 'sshing into '${i}
    #gnome-terminal -x bash -c "ssh -t ${i} 'echo 'hello world!'; bash'" &
    gnome-terminal -x bash -c "ssh -t ${i} 'cd '~/workspace/cs455/hw1/'; 
        ant -Darg0=${HOST} -Darg1=${PORT} node_args; bash'" #&
done
