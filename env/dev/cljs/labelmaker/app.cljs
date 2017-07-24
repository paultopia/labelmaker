(ns ^:figwheel-no-load labelmaker.app
  (:require [labelmaker.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
