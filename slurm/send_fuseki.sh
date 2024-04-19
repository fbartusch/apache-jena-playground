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

HOSTNAMES=$(scontrol show hostnames ${SLURM_JOB_NODELIST} | paste -sd,)

if [ "${KEY}" = "prolog" ]; then
  java -jar /var/home/slurm/FusekiSlurm.jar --prolog --hostnames ${HOSTNAMES}
  exit 0
fi

if [ "${KEY}" = "epilog" ]; then
  java -jar /var/home/slurm/FusekiSlurm.jar --epilog
  exit 0
fi
