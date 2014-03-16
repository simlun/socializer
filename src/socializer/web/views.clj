(ns socializer.web.views
  (:use ring.util.response)
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate app-template "templates/app.html"
  []
  [:head :title] (html/content "Socializer"))

(defn index [] (app-template))

(defn parse-set-of-people [params]
  (let [{people :people} params
        people (clojure.string/split people #"\n")
        people (set people)]
    people))

(defn store-people [session params]
  (let [people (parse-set-of-people params)]
    (-> (redirect-after-post "/people")
        (assoc :session {:people people}))))

(defn list-people [session]
  (response (str session)))
