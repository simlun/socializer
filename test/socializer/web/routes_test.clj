(ns socializer.web.routes-test
  (:use midje.sweet
        ring.mock.request)
  (:require [socializer.web.routes :as sut])
  )

(tabular
  (fact "Status codes are as expected"
      (sut/handler (request :get ?url))
      => (contains {:status ?status}))
  ?url       ?status
  "/notfoundz" 404
  "/"          200
  "/people"    200
  "/groups"    200
  "/events"    200
  "/data"      200)
