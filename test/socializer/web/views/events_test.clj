(ns socializer.web.views.events-test
  (:use midje.sweet)
  (:require [socializer.web.views.events :as events]))

(def tables-str "\nA rectangular 6\n\nC circular 5\nB rectangular 4\n\n")
(def room {"A" {:shape :rectangular
                :nr-chairs 6}
           "B" {:shape :rectangular
                :nr-chairs 4}
           "C" {:shape :circular
                :nr-chairs 5}})
(def unparsed-tables-str "A rectangular 6\nB rectangular 4\nC circular 5")

(def request-params-event-1
  {:event-name "Lunch"
   :date "2014-02-25"
   ;:time "12:00" ;; TODO
   :tables tables-str
   :people "Alice\nAlice\nBob \nCathy\n \nDave\nErin\nFred\nGretl\nHarald\nIrene\nJacob\nKaren\nLinus\nMatilda\nNiel\nOlga"})

(def request-params-event-2
  {:event-name "Dinner"
   :date "2014-02-25"
   ;:time "18:00" ;; TODO
   :tables "A rectangular 6\nB rectangular 4\nC circular 5"
   :people "Alice\n \nBob\nCathy\n\nDave\nErin\nFred\nGretl\nHarald\nIrene\nJacob\nKaren\nLinus\nMatilda\nNiel\nOlga"})

(def participants #{"Alice" "Bob" "Cathy" "Dave" "Erin" "Fred" "Gretl" "Harald"
                    "Irene" "Jacob" "Karen" "Linus" "Matilda" "Niel" "Olga"})

(def event-1 {:event-name "Lunch"
              :date "2014-02-25"
              :tables room
              :participants participants})

(def event-2 {:event-name "Dinner"
              :date "2014-02-25"
              :tables room
              :participants participants})

(def existing-session {:foo 4711})

(def expected-session-1 {:foo 4711
                        :events {"2014-02-25 Lunch" event-1}})

(def expected-session-2 {:foo 4711
                        :events {"2014-02-25 Lunch" event-1
                                 "2014-02-25 Dinner" event-2}})

(fact "A multi line table spec can be parsed to a data structure"
  (events/parse-tables tables-str) => room)

(fact "We can store an event in the session"
      (:session (events/store existing-session
                                request-params-event-1))
      => expected-session-1)

(fact "We are redirected after storing events"
      (events/store existing-session
                      request-params-event-1)
      => (contains {:status 303}))

(fact "Table data can be converted back to string representation"
      (events/unparse-tables room) => unparsed-tables-str)
