(ns socializer.planner-tests
  (:use midje.sweet)
  (:require [socializer.web.views.events-test :as events-test]
            [socializer.planner :as planner]))

(fact "Planning no events returns no seating plans"
      (planner/linear-plan [] #{}) => [])

(def people #{"Alice" "Bob" "Cathy" "Dave" "Erin"
              "Fred" "Gretl" "Harald" "Irene"
              "Jacob" "Karen" "Linus" "Matilda"
              "Niel" "Olga" "Peter"}) ; Peter never gets seated

(def expected-initial-distance-matrix
  {#{"Fred" "Bob"} 1000, #{"Matilda" "Alice"} 1000, #{"Harald" "Matilda"} 1000,
   #{"Harald" "Alice"} 1000, #{"Niel" "Jacob"} 1000, #{"Linus" "Gretl"} 1000,
   #{"Dave" "Bob"} 1000, #{"Peter" "Bob"} 1000, #{"Linus" "Erin"} 1000,
   #{"Harald" "Niel"} 1000, #{"Matilda" "Niel"} 1000, #{"Fred" "Olga"} 1000,
   #{"Alice" "Niel"} 1000, #{"Linus" "Irene"} 1000, #{"Peter" "Olga"} 1000,
   #{"Dave" "Olga"} 1000, #{"Karen" "Jacob"} 1000, #{"Linus" "Bob"} 1000,
   #{"Gretl" "Erin"} 1000, #{"Matilda" "Karen"} 1000, #{"Erin" "Irene"} 1000,
   #{"Alice" "Karen"} 1000, #{"Gretl" "Irene"} 1000, #{"Harald" "Karen"} 1000,
   #{"Cathy" "Jacob"} 1000, #{"Linus" "Olga"} 1000, #{"Erin" "Bob"} 1000,
   #{"Matilda" "Cathy"} 1000, #{"Alice" "Cathy"} 1000, #{"Gretl" "Bob"} 1000,
   #{"Niel" "Karen"} 1000, #{"Harald" "Cathy"} 1000, #{"Irene" "Bob"} 1000,
   #{"Gretl" "Olga"} 1000, #{"Erin" "Olga"} 1000, #{"Niel" "Cathy"} 1000,
   #{"Irene" "Olga"} 1000, #{"Fred" "Jacob"} 1000, #{"Matilda" "Fred"} 1000,
   #{"Dave" "Jacob"} 1000, #{"Alice" "Fred"} 1000, #{"Peter" "Jacob"} 1000,
   #{"Harald" "Fred"} 1000, #{"Matilda" "Peter"} 1000, #{"Alice" "Peter"} 1000,
   #{"Harald" "Dave"} 1000, #{"Bob" "Olga"} 1000, #{"Matilda" "Dave"} 1000,
   #{"Alice" "Dave"} 1000, #{"Harald" "Peter"} 1000, #{"Karen" "Cathy"} 1000,
   #{"Niel" "Fred"} 1000, #{"Linus" "Jacob"} 1000, #{"Niel" "Peter"} 1000,
   #{"Niel" "Dave"} 1000, #{"Harald" "Linus"} 1000, #{"Matilda" "Linus"} 1000,
   #{"Alice" "Linus"} 1000, #{"Karen" "Fred"} 1000, #{"Niel" "Linus"} 1000,
   #{"Karen" "Peter"} 1000, #{"Gretl" "Jacob"} 1000, #{"Karen" "Dave"} 1000,
   #{"Erin" "Jacob"} 1000, #{"Matilda" "Erin"} 1000, #{"Alice" "Erin"} 1000,
   #{"Harald" "Gretl"} 1000, #{"Cathy" "Fred"} 1000, #{"Matilda" "Gretl"} 1000,
   #{"Alice" "Gretl"} 1000, #{"Harald" "Erin"} 1000, #{"Irene" "Jacob"} 1000,
   #{"Matilda" "Irene"} 1000, #{"Cathy" "Peter"} 1000, #{"Alice" "Irene"} 1000,
   #{"Cathy" "Dave"} 1000, #{"Harald" "Irene"} 1000, #{"Karen" "Linus"} 1000,
   #{"Niel" "Gretl"} 1000, #{"Bob" "Jacob"} 1000, #{"Niel" "Erin"} 1000,
   #{"Harald" "Bob"} 1000, #{"Niel" "Irene"} 1000, #{"Matilda" "Bob"} 1000,
   #{"Alice" "Bob"} 1000, #{"Cathy" "Linus"} 1000, #{"Olga" "Jacob"} 1000,
   #{"Karen" "Gretl"} 1000, #{"Harald" "Olga"} 1000, #{"Fred" "Peter"} 1000,
   #{"Niel" "Bob"} 1000, #{"Matilda" "Olga"} 1000, #{"Alice" "Olga"} 1000,
   #{"Karen" "Erin"} 1000, #{"Fred" "Dave"} 1000, #{"Dave" "Peter"} 1000,
   #{"Karen" "Irene"} 1000, #{"Cathy" "Erin"} 1000, #{"Niel" "Olga"} 1000,
   #{"Cathy" "Gretl"} 1000, #{"Karen" "Bob"} 1000, #{"Cathy" "Irene"} 1000,
   #{"Fred" "Linus"} 1000, #{"Dave" "Linus"} 1000, #{"Peter" "Linus"} 1000,
   #{"Cathy" "Bob"} 1000, #{"Karen" "Olga"} 1000, #{"Fred" "Erin"} 1000,
   #{"Fred" "Gretl"} 1000, #{"Peter" "Erin"} 1000, #{"Dave" "Gretl"} 1000,
   #{"Fred" "Irene"} 1000, #{"Peter" "Gretl"} 1000, #{"Dave" "Erin"} 1000,
   #{"Cathy" "Olga"} 1000, #{"Peter" "Irene"} 1000, #{"Harald" "Jacob"} 1000,
   #{"Matilda" "Jacob"} 1000, #{"Dave" "Irene"} 1000, #{"Alice" "Jacob"} 1000})

(fact "The initial distance matrix maps pairs of people to the default distance 1000"
      (planner/create-matrix people) => expected-initial-distance-matrix)

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

(def linear-placements-2
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
   ])

(def linearly-planned-event-1
  {:event {:date "2014-02-25"
           :time "12:00"
           :name "Lunch"}
   :distance {:max 256000
              :current 123000
              :percentage 48}
   :placements linear-placements-1})

(def linearly-planned-event-2
  {:event {:date "2014-02-25"
           :time "18:00"
           :name "Dinner"}
   :distance {:max 256000
              :current 123000
              :percentage 48}
   :placements linear-placements-2})

(fact "Linear table placement places people in alphabetical order"
      (planner/linear-table-placement events-test/participants-1
                                      events-test/room)
      => linear-placements-1)

(fact "Planning one linear event returns a linear seating plan"
      (planner/linear-plan [events-test/event-1] people)
      => [linearly-planned-event-1])

(fact "Planning two linear events returns another linear seating plan"
      (planner/linear-plan [events-test/event-1 events-test/event-2]
                           people)
      => [linearly-planned-event-1 linearly-planned-event-2])
