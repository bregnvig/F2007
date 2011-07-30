#!/bin/bash
endpoint=`cat endpoint.txt`
curl -D - -H "Accept: application/json"  -H "Content-Type: application/json"   $endpoint/season-name