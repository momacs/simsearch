#
# Import statements, etc:
#

import os,sys
sys.path.append(os.path.join(os.path.dirname(__file__), '..', '..'))
import numpy as np
from pram.data     import GroupSizeProbe, ProbeMsgMode, ProbePersistanceDB
from pram.entity   import Group, GroupDBRelSpec, GroupQry, GroupSplitSpec, Site
from pram.rule     import Rule, TimeAlways
from pram.sim      import Simulation

#
# Set parameters:
#

population          = int(sys.argv[1])
initialSusceptible  = int(sys.argv[2])
initialExposed      = int(sys.argv[3])
initialInfectious   = int(sys.argv[4])
R0                  = float(sys.argv[5])
latentPeriod        = float(sys.argv[6])
infectiousPeriod    = float(sys.argv[7])
numberOfDays        = int(sys.argv[8])
numberOfSimulations = int(sys.argv[9])

initialRecovered    = population - (initialSusceptible+initialExposed+initialInfectious)

def printParameters():
  print(population,end=' ')
  print(initialSusceptible,end=' ')
  print(initialExposed,end=' ')
  print(initialInfectious,end=' ')
  print(initialRecovered,end=' ')
  print(R0,end=' ')
  print(latentPeriod,end=' ')
  print(infectiousPeriod,end=' ')
  print(numberOfSimulations,end=' ')
  print(numberOfDays)

#
# Define flu progression rule:
#

siteName = 'Pittsburgh'
site = Site(siteName)

class FluProgressRule(Rule):
    def __init__(self): super().__init__('flu-progress', TimeAlways())
    def apply(self, pop, group, iter, t):
        if group.has_attr({ 'flu': 's' }):
            at  = group.get_rel(Site.AT)
            n   = at.get_pop_size()
            n_s = at.get_pop_size(GroupQry(attr={ 'flu': 's' }))
            n_e = at.get_pop_size(GroupQry(attr={ 'flu': 'e' }))
            n_i = at.get_pop_size(GroupQry(attr={ 'flu': 'i' }))
            beta = R0 / (population*infectiousPeriod)
            lambdat = beta*n_i
            fractionInfected = float(n_i) / float(n)
            numberOfNewlyInfected = np.random.binomial(n_s,lambdat)
            p_infection = float(numberOfNewlyInfected+n_e)/float(n)
            return [
                GroupSplitSpec(p=p_infection, attr_set={ 'flu': 'e' }),
                GroupSplitSpec(p=1-p_infection, attr_set={ 'flu': 's' })
            ]
        if group.has_attr({ 'flu': 'e' }):
            p_transition = 1.0/latentPeriod
            return [
                GroupSplitSpec(p=p_transition, attr_set={ 'flu': 'i' }),
                GroupSplitSpec(p=1.0-p_transition, attr_set={ 'flu': 'e' })
            ]
        if group.has_attr({ 'flu': 'i' }):
            at  = group.get_rel(Site.AT)
            n_i = at.get_pop_size(GroupQry(attr={ 'flu': 'i' }))
            print( n_i, end=' ' )
            p_transition = 1.0/infectiousPeriod
            return [
                GroupSplitSpec(p=p_transition, attr_set={ 'flu': 'r' }),
                GroupSplitSpec(p=1.0-p_transition, attr_set={ 'flu': 'i' })
            ]

#
# Run simulations:
#

def runSimulations(n):
    for n in range(numberOfSimulations):
      (Simulation().
          set().
              pragma_autocompact(True).
              done().
          add([
              FluProgressRule(),
              Group('Susceptible', initialSusceptible, attr={ 'flu': 's' }, rel={ Site.AT: site }),
              Group('Exposed',     initialExposed,     attr={ 'flu': 'e' }, rel={ Site.AT: site }),
              Group('Infectious',  initialInfectious,  attr={ 'flu': 'i' }, rel={ Site.AT: site }),
              Group('Recovered',   initialRecovered,   attr={ 'flu': 'r' }, rel={ Site.AT: site }),
          ]).
          run(numberOfDays)
       )
      print()

runSimulations(numberOfSimulations)

# End-of-File

