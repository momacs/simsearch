# This script runs the PRAM/SEIR simulator with the following arguments:
#
# population susceptible exposed infected R0 latent infectious numberOfDays numberOfSimulations

# The following line must be edited to include path to PRAM bin
# directory where PRAM-customized python3 resides:

PATH="...../PRAM/bin:$PATH"

# The following line must be edited to cd to PRAM directory that
# includes the file seir.py:

cd ...../PRAM/src/sim/seir

# Run PRAM with SEIR simulator and arguments:

python3 ./seir.py $1 $2 $3 $4 $5 $6 $7 $8 $9

