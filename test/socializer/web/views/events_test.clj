(ns socializer.web.views.events-test
  (:use midje.sweet)
  (:require [socializer.web.views.events :as events]))

(def request-params-sample
  {:people "A\n \nB\nC\n\n"})

(def expected-people-set
  #{"A" "B" "C"})

(future-fact "we can store an event in the session"
      (events/->store {} request-params-sample)
      => (contains {:session {:people expected-people-set}}))

(future-fact "we are redirected after storing events"
      (events/->store {} request-params-sample)
      => (contains {:status 303}))

