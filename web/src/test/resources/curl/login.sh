#!/bin/bash
endpoint=`cat endpoint.txt`
curl -D - -H "Accept: application/json" $endpoint/login/flb/flb