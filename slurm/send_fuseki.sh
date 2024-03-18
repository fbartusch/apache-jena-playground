#!/bin/bash

# Parse options
while [[ $# -gt 0 ]]; do
  case $1 in
    --key)
      KEY="$2"
      shift # past argument
      shift # past value
      ;;
    -*|--*)
      echo "Unknown option $1"
      exit 1
      ;;
  esac
done

if [ "${KEY}" = "prolog" ]; then
  java -jar /var/home/slurm/FusekiSlurm.jar prolog
  exit 0
fi

if [ "${KEY}" = "epilog" ]; then
  java -jar /var/home/slurm/FusekiSlurm.jar epilog
  exit 0
fi
