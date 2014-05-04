(ns socializer.table-tests
  (:use midje.sweet)
  (:require [socializer.web.views.events-test :as events-test]
            [socializer.table :as table]
            [socializer.planner :as planner]
            [socializer.planner-tests :as planner-tests]))

(fact "Sorted table placement places people in alphabetical order"
      (table/sorted-table-placement events-test/participants-1
                                    events-test/room
                                    nil)
      => planner-tests/sorted-placements-1)
