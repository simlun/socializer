(ns socializer.planner-tests
  (:use midje.sweet)
  (:require [socializer.web.views.events-test :as events-test]
            [socializer.planner :as planner]))

(fact "Planning no events returns no seating plans"
      (planner/linear-plan []) => [])

(def participants #{"Alice" "Bob" "Cathy" "Dave" "Erin"
                    "Fred" "Gretl" "Harald" "Irene"
                    "Jacob" "Karen" "Linus" "Matilda"
                    "Niel" "Olga"})

(def linear-placements-1
  [{:person-name "Alice" :table-name "A" :chair 0}
   {:person-name "Bob" :table-name "A" :chair 1}
   {:person-name "Cathy" :table-name "A" :chair 2}
   {:person-name "Dave" :table-name "A" :chair 3}
   {:person-name "Erin" :table-name "A" :chair 4}
   {:person-name "Fred" :table-name "A" :chair 5}
   {:person-name "Gretl" :table-name "B" :chair 0}
   {:person-name "Harald" :table-name "B" :chair 1}
   {:person-name "Irene" :table-name "B" :chair 2}
   {:person-name "Jacob" :table-name "B" :chair 3}
   {:person-name "Karen" :table-name "C" :chair 0}
   {:person-name "Linus" :table-name "C" :chair 1}
   {:person-name "Matilda" :table-name "C" :chair 2}
   {:person-name "Niel" :table-name "C" :chair 3}
   {:person-name "Olga" :table-name "C" :chair 4}])

(def linearly-planned-event-1
  {:event {:date "2014-02-25"
           :time "12:00"
           :name "Lunch"}
   :placements linear-placements-1})

(def linearly-planned-event-2
  {:event {:date "2014-02-25"
           :time "18:00"
           :name "Dinner"}
   :placements linear-placements-1})

(fact "Linear table placement places in alphabetical order"
      (planner/linear-table-placement events-test/participants
                                      events-test/room)
      => linear-placements-1)

(fact "Planning one linear event returns a linear seating plan"
      (planner/linear-plan [events-test/event-1])
      => [linearly-planned-event-1])

(fact "Planning two linear events returns another linear seating plan"
      (planner/linear-plan [events-test/event-1 events-test/event-2])
      => [linearly-planned-event-1 linearly-planned-event-2])
