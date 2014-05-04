(ns socializer.planner-tests
  (:use midje.sweet)
  (:require [socializer.web.views.events-test :as events-test]
            [socializer.table :as table]
            [socializer.planner :as planner]))

(fact "Planning no events returns no seating plans"
      (planner/plan [] #{} {}) => [])

(def people #{"Alice" "Bob" "Cathy" "Dave" "Erin"
              "Fred" "Gretl" "Harald" "Irene"
              "Jacob" "Karen" "Linus" "Matilda"
              "Niel" "Olga" "Peter"}) ; Peter never gets seated

(def sorted-placements-1
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

(def groups {"Asocial" {:members #{"Alice" "Bob"}}
             "Bearded" {:members #{"Bob" "Dave" "Niel"}}})

(def group-member-pairs
  '(#{"Alice" "Bob"}
    #{"Dave" "Bob"} #{"Niel" "Dave"} #{"Niel" "Bob"}))

(fact "create a list of sets of each pair of people of groups"
      (planner/create-group-member-pairs groups)
      => group-member-pairs)

(def placements-1-person-name-set-per-table
  '(#{"Alice" "Bob" "Cathy" "Dave" "Erin" "Fred"}
    #{"Gretl" "Harald" "Irene" "Jacob"}
    #{"Karen" "Linus" "Matilda" "Niel" "Olga"}))

(fact "grouping people per table in sets"
      (planner/create-person-name-set-per-table sorted-placements-1)
      => placements-1-person-name-set-per-table)

(fact "counting the grouped pairs of people: Alice-Bob, Bob-Dave, Bob-Niel, Dave-Niel"
      (planner/grouped-pair-count {}) => 0
      (planner/grouped-pair-count groups) => 4)

(fact "counting the satisfied groupings in a placement"
      (planner/count-satisfied-groupings #{} {}) => 0
      (planner/count-satisfied-groupings sorted-placements-1 groups) => 2)

(fact "groups of a single person is never satisfied"
      (planner/count-satisfied-groupings sorted-placements-1 {"Outsider" {:members #{"Alice"}}}) => 0)

(def sorted-placements-2
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
   {:person-name "Niel" :table-name "C" :chair 3}])

(def sorted-planned-event-1
  {:event {:date "2014-02-25"
           :time "12:00"
           :name "Lunch"}
   :distance {:max 120000
              :current 89000
              :percentage 74}
   :groupings {:pairwise-total 4
               :pairwise-satisfied 2
               :percentage-satisfied 50}
   :placements sorted-placements-1})

(def sorted-planned-event-2
  {:event {:date "2014-02-25"
           :time "18:00"
           :name "Dinner"}
   :distance {:max 120000
              :current 89000
              :percentage 74}
   :groupings {:pairwise-total 4
               :pairwise-satisfied 2
               :percentage-satisfied 50}
   :placements sorted-placements-2})

(fact "Planning one sorted event returns a sorted seating plan"
      (planner/plan [events-test/event-1] people groups)
      => [sorted-planned-event-1])

(fact "Planning two sorted events returns another sorted seating plan"
      (planner/plan [events-test/event-1 events-test/event-2]
                    people
                    groups)
      => [sorted-planned-event-1 sorted-planned-event-2])
