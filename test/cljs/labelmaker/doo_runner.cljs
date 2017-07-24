(ns labelmaker.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [labelmaker.core-test]))

(doo-tests 'labelmaker.core-test)

