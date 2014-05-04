(ns socializer.distance-tests
  (:use midje.sweet)
  (:require [socializer.web.views.events-test :as events-test]
            [socializer.distance :as distance]
            [socializer.planner :as planner]
            [socializer.planner-tests :as planner-tests]))

(fact "a set of pairs of things can be created"
      (distance/create-pairs #{}) => nil
      (distance/create-pairs [1 2 3]) => [#{1 2}
                                          #{1 3}
                                          #{2 3}]
      (distance/create-pairs #{"A" "B" "C"}) => [#{"A" "B"}
                                                 #{"A" "C"}
                                                 #{"B" "C"}])

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
      (distance/create-matrix planner-tests/people) => expected-initial-distance-matrix)

(def chair-agnostic-table-distance-matrix-B
  {#{"Harald" "Gretl"} 0
   #{"Irene" "Jacob"} 0
   #{"Jacob" "Harald"} 0
   #{"Gretl" "Irene"} 0
   #{"Gretl" "Jacob"} 0
   #{"Harald" "Irene"} 0})

(fact "Sitting at the same table sets their social distance to 0"
      (distance/judge-table ["B" (get events-test/room "B")]
                            planner-tests/sorted-placements-1)
      => chair-agnostic-table-distance-matrix-B)
