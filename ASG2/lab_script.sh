#!/bin/bash

HOST=$(hostname)
PORT="8082" # default registry port

ant clean

# registry start-up
gnome-terminal -x bash -c "ant -Darg0=${PORT} server; bash"
