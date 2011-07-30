#!/bin/bash
endpoint=`cat endpoint.txt`/login/flb/flb
echo $endpoint
curl -D - -H "Accept: application/json" $endpoint