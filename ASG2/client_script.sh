#!/bin/bash

HOST=$(hostname)
PORT="8082" # default registry port
for i in `cat machines_20`
do
    echo 'sshing into '${i}
    #gnome-terminal -x bash -c "ssh -t ${i} 'echo 'hello world!'; bash'" &
    gnome-terminal -x bash -c "ssh -t ${i} 'cd '~/workspace/cs455/ASG2'; 
        echo $HOSTNAME; ant -Darg0=${HOST} -Darg1=${PORT} client; bash'" #&
done
