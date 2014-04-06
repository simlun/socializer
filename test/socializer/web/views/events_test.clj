(ns socializer.web.views.events-test
  (:use midje.sweet)
  (:require [socializer.web.views.events :as events]))

(def tables-str "\nA rect 6\n\nC circ 5\nB rect 4\n\n")
(def room {"A" {:shape :rect
                :nr-chairs 6}
           "B" {:shape :rect
                :nr-chairs 4}
           "C" {:shape :circ
                :nr-chairs 5}})
(def unparsed-tables-str "A rect 6\nB rect 4\nC circ 5")

(fact "A multi line table spec can be parsed to a data structure"
  (events/parse-tables tables-str) => room)

(fact "Table data can be converted back to string representation"
      (events/unparse-tables room) => unparsed-tables-str)

(def request-params-event-1
  {:event-name "Lunch"
   :date "2014-02-25"
   :hour "12"
   :minute "00"
   :tables tables-str
   :people "Alice\nAlice\nBob \nCathy\n \nDave\nErin\nFred\nGretl\nHarald\nIrene\nJacob\nKaren\nLinus\nMatilda\nNiel\nOlga"})

(def request-params-event-2
  {:event-name "Dinner"
   :date "2014-02-25"
   :hour "18"
   :minute "00"
   :tables "A rect 6\nB rect 4\nC circ 5"
   :people "Alice\n \nBob\nCathy\n\nDave\nErin\nFred\nGretl\nHarald\nIrene\nJacob\nKaren\nLinus\nMatilda\nNiel"})

(def participants-1 #{"Alice" "Bob" "Cathy" "Dave" "Erin"
                    "Fred" "Gretl" "Harald" "Irene"
                    "Jacob" "Karen" "Linus" "Matilda"
                    "Niel" "Olga"})

(def participants-2 #{"Alice" "Bob" "Cathy" "Dave" "Erin"
                    "Fred" "Gretl" "Harald" "Irene"
                    "Jacob" "Karen" "Linus" "Matilda"
                    "Niel"}) ; Without Olga!

(def event-1 {:event-name "Lunch"
              :date "2014-02-25"
              :time "12:00"
              :tables room
              :participants participants-1})

(def event-2 {:event-name "Dinner"
              :date "2014-02-25"
              :time "18:00"
              :tables room
              :participants participants-2})

(def existing-session {:foo 4711})

(def expected-session-1 {:foo 4711
                         :events {"2014-02-25 12:00 Lunch" event-1}})

(def expected-session-2 {:foo 4711
                         :events {"2014-02-25 12:00 Lunch" event-1
                                  "2014-02-25 18:00 Dinner" event-2}})

(fact "We can store an event in the session"
      (:session (events/store existing-session
                              request-params-event-1))
      => expected-session-1)

(fact "We can store another event in the session too"
      (:session (events/store expected-session-1
                              request-params-event-2))
      => expected-session-2)

(fact "We are redirected after storing events"
      (events/store existing-session
                    request-params-event-1)
      => (contains {:status 303}))
