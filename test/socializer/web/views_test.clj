(ns socializer.web.views-test
  (:use midje.sweet)
  (:require [socializer.web.views :as views])
  )

(fact "we can store a set of people in the session"
      (views/store-people {} {:people "A\nB\nC"})
      => (contains {:session {:people #{"A" "B" "C"}}}))
