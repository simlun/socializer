(ns socializer.web.views
  (:use clojure.pprint
        ring.util.response
        )
  )

(defn index [] "Hello, World Wide Web!")

(defn parse-set-of-people [params]
  (let [{people :people} params
        people (clojure.string/split people #"\n")
        people (set people)]
    people))

(defn store-people [session params]
  (let [people (parse-set-of-people params)]
    (-> (response "Foo")
        (assoc :session {:people people}))))

(defn list-people [session]
  (pprint session)
  (response (str session)))
