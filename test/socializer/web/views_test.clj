(ns socializer.web.views-test
  (:use midje.sweet)
  (:require [socializer.web.views :as views])
  )

(def request-params-sample
  {:people "A\n \nB\nC\n\n"})

(def expected-people-set
  #{"A" "B" "C"})

(fact "we can extract a set of people from request params"
      (views/parse-set-of-people request-params-sample)
      => expected-people-set)

(fact "we can store a set of people in the session"
      (views/store-people {} request-params-sample)
      => (contains {:session {:people expected-people-set}}))

(fact "we are redirected after storing people"
      (views/store-people {} request-params-sample)
      => (contains {:status 303}))
