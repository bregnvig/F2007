#!/bin/bash
endpoint=`cat endpoint.txt`/login/flb/flb
curl -D - -H "Accept: application/json" $endpoint