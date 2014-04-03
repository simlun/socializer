(ns socializer.web.views.data-test
  (:use midje.sweet)
  (:require [socializer.web.views.data :as data]))

(def existing-session {:foo 4711})
(def request-params {:data "{:bar 17}"})
(def expected-session {:bar 17})

(fact "We can replace the session content"
      (:session (data/store existing-session request-params))
      => expected-session)

(fact "We are redirected after storing data"
      (data/store existing-session request-params)
      => (contains {:status 303}))
