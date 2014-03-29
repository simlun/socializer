(ns socializer.planner-tests
  (:use midje.sweet)
  (:require [socializer.planner :as planner]))

(def plan planner/linear-plan)

(def events {})

(fact "Planning no events returns no seating plans"
      (plan {}) => {})

(future-fact "Planning one event with one participant returns that seating plan"
      (plan {"2014-02-25 Lunch" {:event-name "Lunch"
                                 :date "2014-02-25"
                                 :tables {"A" {:shape :rectangular
                                               :nr-chairs 1}}
                                 :participants #{"John Doe"}
                                 }})
      => {"2014-02-25 Lunch" {"John Doe" {:table-name "A"
                                          :chair-index 0}}})

; TODO Event with more participants than chairs
