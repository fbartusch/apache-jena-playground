#!/bin/bash

# Validate the examples in this directory
# --validate is same as Same as --sink --check --strict
# As there are problems with checking URIs, we omit --check
# The directory contains only formats supported by riot. ProvToolbox can output more formats.

# Examples were taken from this repository:
# https://github.com/fbartusch/ProvToolbox-playground/tree/master/example_output

### JSON-LD
sudo docker run -v \
  -it \
  -v "$(pwd)"/:/input \
  jena:latest \
  riot --strict --sink /input/fmri_provenance.jsonld
# Returns:
# 06:05:19 ERROR riot :: There was a problem encountered loading a remote context [code=LOADING_REMOTE_CONTEXT_FAILED].


### JSON
sudo docker run -v \
  -it \
  -v "$(pwd)"/:/input \
  jena:latest \
  riot --syntax=RDF/XML --nobase --strict --sink /input/fmri_provenance.xml
# Returns:
# 06:14:11 ERROR riot            :: [line: 5, col: 10] {E201} The attributes on this property element, are not permitted with any content; expecting end element tag.

### JSON
sudo docker run -v \
  -it \
  -v "$(pwd)"/:/input \
  jena:latest \
  riot --syntax=RDF/JSON --nobase --strict --sink /input/fmri_provenance.json
# Returns:
# 06:11:48 ERROR riot            :: [line: 3, col: 13] Expected a [ character to start a JSON Array for the Object List but got [STRING:http://www.w3.org/2001/XMLSchema#]


### Turtle
sudo docker run -v \
  -it \
  -v "$(pwd)"/:/input \
  jena:latest \
  riot --validate /input/fmri_provenance.ttl
# Returns: nothing -> no errors

# That means the Turtle serialization by ProvToolbox
# is the only one that riot doesn't complain about.