(ns socializer.web.views.people-test
  (:use midje.sweet)
  (:require [socializer.web.views.people :as people]))

(def request-params-sample
  {:people "A\n \nB\nC\n\n"})

(def expected-people-set
  #{"A" "B" "C"})

(fact "we can extract a set of people from request params"
      (people/parse-set-of-people request-params-sample)
      => expected-people-set)

(fact "we can store a set of people in the session"
      (people/store-people {} request-params-sample)
      => (contains {:session {:people expected-people-set}}))

(fact "we are redirected after storing people"
      (people/store-people {} request-params-sample)
      => (contains {:status 303}))
