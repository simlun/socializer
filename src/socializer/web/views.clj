(ns socializer.web.views
  (:use clojure.pprint
        ring.util.response
        )
  )

(defn index [] "Hello, World Wide Web!")

(defn store-people [session params]
  (println)
  (pprint session)
  (pprint params)
  (let [{people :people} params
        people (clojure.string/split people #"\n")
        people (set people)]
    (pprint people)
    (-> (response "Foo")
        (assoc :session {:people people}))))

(defn list-people [session]
  (pprint session)
  (response (str session)))
